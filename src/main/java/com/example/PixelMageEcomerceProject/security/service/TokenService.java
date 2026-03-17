package com.example.PixelMageEcomerceProject.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final StringRedisTemplate redisTemplate;

    // =========================================================
    // Key prefix constants — tránh xung đột key trong Redis
    // =========================================================
    private static final String PREFIX_REFRESH     = "refresh_token:";   // refresh_token:{token} → email
    private static final String PREFIX_USER_REFRESH = "user_refresh:";   // user_refresh:{email} → token (để revoke cũ khi login mới)
    private static final String PREFIX_BLACKLIST   = "blacklist:";        // blacklist:{accessToken} → "1"
    private static final String PREFIX_VERIFY      = "verify_token:";    // verify_token:{token} → email

    // TTL
    private static final Duration REFRESH_TOKEN_TTL    = Duration.ofDays(30);
    private static final Duration ACCESS_TOKEN_TTL     = Duration.ofHours(24); // Phải khớp với jwt.expiration
    private static final Duration VERIFY_TOKEN_TTL     = Duration.ofHours(24);

    // =========================================================
    // Verification token — dùng cho email verify khi đăng ký
    // =========================================================

    /**
     * Tạo verification token và lưu Redis
     * Key: verify_token:{token} → email
     */
    public String generateVerificationToken(String email) {
        String token = generateSecureToken();
        redisTemplate.opsForValue().set(
                PREFIX_VERIFY + token,
                email,
                VERIFY_TOKEN_TTL
        );
        log.debug("Verification token created for email: {}", email);
        return token;
    }

    /**
     * Lấy email từ verification token, xóa token sau khi dùng (one-time use)
     * @return email nếu token hợp lệ, null nếu hết hạn hoặc không tồn tại
     */
    public String consumeVerificationToken(String token) {
        String key = PREFIX_VERIFY + token;
        String email = redisTemplate.opsForValue().get(key);
        if (email != null) {
            redisTemplate.delete(key); // One-time use
            log.debug("Verification token consumed for email: {}", email);
        }
        return email;
    }

    // =========================================================
    // Refresh token — lưu Redis, quản lý 1 token/user
    // =========================================================

    /**
     * Tạo refresh token cho user.
     * Tự động revoke token cũ nếu user đã có (1 user = 1 refresh token active).
     * Key 1: refresh_token:{token} → email (lookup khi refresh)
     * Key 2: user_refresh:{email} → token (để revoke khi login lại)
     */
    public String generateRefreshToken(String email) {
        // Revoke token cũ nếu có
        revokeUserRefreshToken(email);

        String token = generateSecureToken();

        // Lưu 2 chiều để có thể lookup cả 2 hướng
        redisTemplate.opsForValue().set(PREFIX_REFRESH + token, email, REFRESH_TOKEN_TTL);
        redisTemplate.opsForValue().set(PREFIX_USER_REFRESH + email, token, REFRESH_TOKEN_TTL);

        log.debug("Refresh token created for email: {}", email);
        return token;
    }

    /**
     * Validate refresh token và trả về email nếu hợp lệ
     * KHÔNG xóa token — refresh token dùng được nhiều lần cho đến khi hết hạn hoặc bị revoke
     */
    public String validateRefreshToken(String token) {
        return redisTemplate.opsForValue().get(PREFIX_REFRESH + token);
    }

    /**
     * Revoke refresh token của user (dùng khi logout hoặc login lại)
     */
    public void revokeUserRefreshToken(String email) {
        String existingToken = redisTemplate.opsForValue().get(PREFIX_USER_REFRESH + email);
        if (existingToken != null) {
            redisTemplate.delete(PREFIX_REFRESH + existingToken);
            redisTemplate.delete(PREFIX_USER_REFRESH + email);
            log.debug("Refresh token revoked for email: {}", email);
        }
    }

    /**
     * Revoke refresh token bằng token value (dùng khi logout từ client gửi token lên)
     */
    public void revokeRefreshToken(String token) {
        String email = redisTemplate.opsForValue().get(PREFIX_REFRESH + token);
        if (email != null) {
            redisTemplate.delete(PREFIX_REFRESH + token);
            redisTemplate.delete(PREFIX_USER_REFRESH + email);
            log.debug("Refresh token revoked: {}", token);
        }
    }

    // =========================================================
    // Access token blacklist — dùng khi logout chủ động
    // =========================================================

    /**
     * Blacklist access token (khi user logout)
     * TTL = thời gian còn lại của token để tự động cleanup
     * @param token JWT access token
     * @param remainingMillis thời gian còn lại của token tính bằng ms
     */
    public void blacklistAccessToken(String token, long remainingMillis) {
        if (remainingMillis > 0) {
            redisTemplate.opsForValue().set(
                    PREFIX_BLACKLIST + token,
                    "1",
                    Duration.ofMillis(remainingMillis)
            );
            log.debug("Access token blacklisted, expires in {}ms", remainingMillis);
        }
    }

    /**
     * Kiểm tra token có trong blacklist không
     */
    public boolean isAccessTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(PREFIX_BLACKLIST + token));
    }

    // =========================================================
    // Private helper
    // =========================================================

    /**
     * Tạo secure random token 32 bytes, encode URL-safe Base64
     */
    private String generateSecureToken() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
