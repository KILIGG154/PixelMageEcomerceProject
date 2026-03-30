package com.example.PixelMageEcomerceProject.security.oauth2;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.PixelMageEcomerceProject.entity.Account;
import com.example.PixelMageEcomerceProject.entity.Role;
import com.example.PixelMageEcomerceProject.enums.AuthProvider;
import com.example.PixelMageEcomerceProject.repository.AccountRepository;
import com.example.PixelMageEcomerceProject.repository.RoleRepository;
import com.example.PixelMageEcomerceProject.security.jwt.JwtTokenProvider;
import com.example.PixelMageEcomerceProject.security.service.TokenService;
import com.example.PixelMageEcomerceProject.service.EmailService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        try {
            Map<String, Object> attributes = oAuth2User.getAttributes();
            String email = (String) attributes.get("email");
            String name = (String) attributes.get("name");
            String googleId = (String) attributes.get("sub");
            String avatarUrl = (String) attributes.get("picture");
            Boolean emailVerified = (Boolean) attributes.get("email_verified");

            // Google chưa verify email thì không cho vào
            if (!Boolean.TRUE.equals(emailVerified)) {
                log.warn("Google account with unverified email attempted login: {}", email);
                String errorUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/error")
                        .queryParam("error", "email_not_verified")
                        .queryParam("message", "Email Google chưa được xác thực")
                        .build()
                        .encode()
                        .toUriString();
                getRedirectStrategy().sendRedirect(request, response, errorUrl);
                return;
            }

            log.info("Processing OAuth2 authentication for user: {}", email);
            Account account = processOAuth2Account(email, name, googleId, avatarUrl);

            String accessToken = jwtTokenProvider.generateToken(account);
            String refreshToken = tokenService.generateRefreshToken(account.getEmail());

            // Ưu tiên dùng redirect_uri từ cookie (nếu FE truyền lên lúc bắt đầu login)
            String targetUrl = cookieAuthorizationRequestRepository.getRedirectUriFromCookie(request)
                    .orElse(frontendUrl + "/success");

            String redirectUrl = UriComponentsBuilder.fromUriString(targetUrl)
                    .fragment("accessToken=" + accessToken
                            + "&refreshToken=" + refreshToken
                            + "&email=" + URLEncoder.encode(account.getEmail(), StandardCharsets.UTF_8.toString())
                            + "&name=" + URLEncoder.encode(account.getName(), StandardCharsets.UTF_8.toString()))
                    .build().toUriString();

            // Clear cookies an toàn trước khi đi
            cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);

            log.info("OAuth2 login success, redirecting to: {}", targetUrl);
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);

        } catch (Exception e) {
            log.error("Error processing OAuth2 authentication", e);
            String errorUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/error")
                    .queryParam("error", "authentication_failed")
                    .queryParam("message", "Đăng nhập Google thất bại, vui lòng thử lại")
                    .build()
                    .encode()
                    .toUriString();
            getRedirectStrategy().sendRedirect(request, response, errorUrl);
        }
    }

    @Transactional
    private Account processOAuth2Account(String email, String name, String googleId, String avatarUrl) {
        Optional<Account> existing = accountRepository.findByEmailIgnoreActive(email);

        if (existing.isPresent()) {
            return linkOAuth2Provider(existing.get(), googleId, avatarUrl);
        } else {
            return createOAuth2Account(email, name, googleId, avatarUrl);
        }
    }

    private Account linkOAuth2Provider(Account account, String providerId, String avatarUrl) {
        boolean changed = false;

        if (account.getAuthProvider() == AuthProvider.LOCAL && Boolean.TRUE.equals(account.getIsActive())) {
            account.setAuthProvider(AuthProvider.GOOGLE);
            account.setProviderId(providerId);
            // Gửi mail thông báo account LOCAL vừa được link với Google
            emailService.sendGoogleLinkedNotification(account.getEmail(), account.getName());
            changed = true;
            log.info("Linked Google to existing LOCAL account: {}", account.getEmail());
        } else if (!providerId.equals(account.getProviderId())) {
            account.setProviderId(providerId);
            changed = true;
        }

        // Account Google luôn được coi là verified
        if (!Boolean.TRUE.equals(account.getEmailVerified())) {
            account.setEmailVerified(true);
            changed = true;
        }

        // Update avatar nếu chưa có
        if (account.getAvatarUrl() == null && avatarUrl != null) {
            account.setAvatarUrl(avatarUrl);
            changed = true;
        }

        return changed ? accountRepository.save(account) : account;
    }

    private Account createOAuth2Account(String email, String name, String googleId, String avatarUrl) {
        Role userRole = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new RuntimeException("USER role not found"));

        Account newAccount = new Account();
        newAccount.setEmail(email);
        newAccount.setName(name);
        newAccount.setAuthProvider(AuthProvider.GOOGLE);
        newAccount.setProviderId(googleId);
        newAccount.setAvatarUrl(avatarUrl);
        newAccount.setRole(userRole);
        // Google đã verify email → set luôn
        newAccount.setEmailVerified(true);
        // Password null cho OAuth2 account

        log.info("Creating new Google OAuth2 account: {}", email);
        return accountRepository.save(newAccount);
    }
}
