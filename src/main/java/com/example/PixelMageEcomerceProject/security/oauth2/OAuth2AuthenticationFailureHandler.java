package com.example.PixelMageEcomerceProject.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

/**
 * OAuth2 Authentication Failure Handler
 * Handles failed Google OAuth2 authentication attempts
 */
@Slf4j
@Component
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        
        log.error("OAuth2 authentication failed", exception);

        String errorUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/auth/error")
            .queryParam("error", "oauth2_failed")
            .queryParam("message", exception.getMessage())
            .build()
            .toUriString();

        getRedirectStrategy().sendRedirect(request, response, errorUrl);
    }
}