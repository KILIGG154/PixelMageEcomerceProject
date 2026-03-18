package com.example.PixelMageEcomerceProject.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend.url}")
    private String frontendUrl;


    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * Gửi email verify account sau khi đăng ký LOCAL
     *
     * @Async để không block HTTP response — user nhận response ngay,
     *        mail gửi nền
     */
    @Async
    public void sendVerificationEmail(String toEmail, String name, String token) {
        String verifyUrl = frontendUrl + "/auth/verify?token=" + token;

        String html = """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto;">
                    <h2 style="color: #4A90E2;">Xác thực tài khoản PixelMage</h2>
                    <p>Xin chào <strong>%s</strong>,</p>
                    <p>Cảm ơn bạn đã đăng ký. Vui lòng click vào nút bên dưới để xác thực email:</p>
                    <a href="%s"
                       style="display:inline-block; padding:12px 24px; background:#4A90E2;
                              color:#fff; text-decoration:none; border-radius:6px; margin:16px 0;">
                        Xác thực email
                    </a>
                    <p style="color:#888; font-size:13px;">
                        Link có hiệu lực trong 24 giờ.<br>
                        Nếu bạn không đăng ký tài khoản này, hãy bỏ qua email này.
                    </p>
                </div>
                """.formatted(name, verifyUrl);

        sendHtmlEmail(toEmail, "Xác thực tài khoản PixelMage", html);
    }

    /**
     * Gửi email thông báo khi tài khoản LOCAL được link với Google
     */
    @Async
    public void sendGoogleLinkedNotification(String toEmail, String name) {
        String html = """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto;">
                    <h2 style="color: #4A90E2;">Tài khoản đã được liên kết với Google</h2>
                    <p>Xin chào <strong>%s</strong>,</p>
                    <p>Tài khoản của bạn vừa được liên kết thành công với Google.</p>
                    <p>Từ bây giờ bạn có thể đăng nhập bằng cả email/mật khẩu lẫn Google.</p>
                    <p style="color:#888; font-size:13px;">
                        Nếu bạn không thực hiện hành động này, hãy đổi mật khẩu ngay lập tức.
                    </p>
                </div>
                """.formatted(name);

        sendHtmlEmail(toEmail, "Tài khoản PixelMage đã liên kết Google", html);
    }

    /**
     * Gửi email reset password
     */
    @Async
    public void sendResetPasswordEmail(String toEmail, String name, String token) {
        String resetUrl = frontendUrl + "/auth/reset-password?token=" + token;

        String html = """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto;">
                    <h2 style="color: #E24A4A;">Đặt lại mật khẩu PixelMage</h2>
                    <p>Xin chào <strong>%s</strong>,</p>
                    <p>Chúng tôi nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn.</p>
                    <a href="%s"
                       style="display:inline-block; padding:12px 24px; background:#E24A4A;
                              color:#fff; text-decoration:none; border-radius:6px; margin:16px 0;">
                        Đặt lại mật khẩu
                    </a>
                    <p style="color:#888; font-size:13px;">
                        Link có hiệu lực trong 1 giờ.<br>
                        Nếu bạn không yêu cầu, hãy bỏ qua email này.
                    </p>
                </div>
                """.formatted(name, resetUrl);

        sendHtmlEmail(toEmail, "Đặt lại mật khẩu PixelMage", html);
    }

    // =========================================================
    // Private helper
    // =========================================================

    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = html
            mailSender.send(message);
            log.info("Email sent to: {}", to);
        } catch (MessagingException e) {
            // Log lỗi nhưng không throw — mail thất bại không nên crash flow chính
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }
}
