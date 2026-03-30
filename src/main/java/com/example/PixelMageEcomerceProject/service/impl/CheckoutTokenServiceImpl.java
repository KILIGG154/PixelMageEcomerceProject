package com.example.PixelMageEcomerceProject.service.impl;

import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory, one-use Checkout Token store.
 *
 * Properties:
 *  - Short TTL: 5 minutes from issuance
 *  - One-use: burned (deleted) on first successful verification
 *  - Stored only in the JVM heap — not persisted, not replicated across pods
 *    (acceptable for a single-instance MVP; swap to Redis for multi-pod deploy)
 */
@Service
public class CheckoutTokenServiceImpl {

    private static final long TTL_MS = 5 * 60 * 1000L; // 5 minutes

    /** Value holds the email (username) and the expiry epoch-ms */
    private final Map<String, Entry> store = new ConcurrentHashMap<>();

    private record Entry(String email, long expiresAt) {}

    // ------------------------------------------------------------------
    // Public API
    // ------------------------------------------------------------------

    /** Issue a new one-use checkout token for the given email. */
    public String issue(String email) {
        evictExpired();
        String token = "ct_" + UUID.randomUUID().toString().replace("-", "");
        store.put(token, new Entry(email, System.currentTimeMillis() + TTL_MS));
        return token;
    }

    /**
     * Verify and consume the token.
     *
     * @return the email (username) associated with the token
     * @throws IllegalArgumentException if token is invalid, expired, or already used
     */
    public String verifyAndConsume(String token) {
        Entry entry = store.remove(token); // atomic remove — one-use guaranteed
        if (entry == null) {
            throw new IllegalArgumentException("Checkout token không hợp lệ hoặc đã được sử dụng.");
        }
        if (System.currentTimeMillis() > entry.expiresAt()) {
            throw new IllegalArgumentException("Checkout token đã hết hạn (5 phút).");
        }
        return entry.email();
    }

    // ------------------------------------------------------------------
    // Housekeeping
    // ------------------------------------------------------------------

    private void evictExpired() {
        long now = System.currentTimeMillis();
        Iterator<Map.Entry<String, Entry>> it = store.entrySet().iterator();
        while (it.hasNext()) {
            if (it.next().getValue().expiresAt() < now) {
                it.remove();
            }
        }
    }
}
