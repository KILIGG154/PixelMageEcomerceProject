package com.example.PixelMageEcomerceProject.enums;

/**
 * Status machine for UnlinkRequest (TASK-05).
 *
 * PENDING_EMAIL   → Customer đã tạo request, chờ click email verify.
 * EMAIL_CONFIRMED → Customer đã click link → Queue hiện lên cho Staff.
 * APPROVED        → Staff approve → unlinkCard() chain đã thực thi.
 * REJECTED        → Staff reject → Customer được notify qua email.
 */
public enum UnlinkRequestStatus {
    PENDING_EMAIL,
    EMAIL_CONFIRMED,
    APPROVED,
    REJECTED
}
