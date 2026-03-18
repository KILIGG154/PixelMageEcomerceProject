package com.example.PixelMageEcomerceProject.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.PixelMageEcomerceProject.dto.request.RegisterRequestDTO;
import com.example.PixelMageEcomerceProject.dto.request.UpdateProfileRequestDTO;
import com.example.PixelMageEcomerceProject.dto.request.ChangePasswordRequestDTO;
import com.example.PixelMageEcomerceProject.dto.request.ForgotPasswordRequestDTO;
import com.example.PixelMageEcomerceProject.dto.request.ResetPasswordRequestDTO;
import com.example.PixelMageEcomerceProject.dto.request.LoginRequestDTO;
import com.example.PixelMageEcomerceProject.dto.response.ResponseBase;
import com.example.PixelMageEcomerceProject.entity.Account;
import com.example.PixelMageEcomerceProject.enums.AuthProvider;
import com.example.PixelMageEcomerceProject.security.jwt.JwtTokenProvider;
import com.example.PixelMageEcomerceProject.security.service.AuthenticationService;
import com.example.PixelMageEcomerceProject.service.interfaces.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "Account", description = "Quản lý tài khoản và xác thực")
@SecurityRequirement(name = "bearerAuth")
public class AccountController {

        private final AccountService accountService;
        private final AuthenticationService authenticationService;
        private final JwtTokenProvider jwtTokenProvider;

        // =========================================================
        // Auth — public endpoints
        // =========================================================

        @PostMapping("/auth/registration")
        @Operation(summary = "Đăng ký tài khoản", description = "Sau khi đăng ký thành công, hệ thống gửi email xác thực. Account chưa verify không thể đăng nhập.")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Đăng ký thành công, chờ xác thực email"),
                        @ApiResponse(responseCode = "400", description = "Email đã tồn tại hoặc dữ liệu không hợp lệ")
        })
        public ResponseEntity<ResponseBase<Account>> createAccount(@RequestBody RegisterRequestDTO dto) {
                try {
                        return ResponseBase.created(
                                        accountService.createAccount(dto),
                                        "Đăng ký thành công. Vui lòng kiểm tra email để xác thực tài khoản.");
                } catch (RuntimeException e) {
                        return ResponseBase.error(HttpStatus.BAD_REQUEST, e.getMessage());
                }
        }

        @PostMapping("/auth/login")
        @Operation(summary = "Đăng nhập", description = "Trả về accessToken (24h) và refreshToken (30 ngày). Account phải verify email trước.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Đăng nhập thành công, trả về accessToken + refreshToken"),
                        @ApiResponse(responseCode = "401", description = "Sai email/password hoặc email chưa được xác thực")
        })
        public ResponseEntity<ResponseBase<Map<String, Object>>> loginAccount(@RequestBody LoginRequestDTO dto) {
                try {
                        return ResponseBase.ok(accountService.loginAccount(dto), "Đăng nhập thành công");
                } catch (RuntimeException e) {
                        return ResponseBase.error(HttpStatus.UNAUTHORIZED, e.getMessage());
                }
        }

        @GetMapping("/auth/verify")
        @Operation(summary = "Xác thực email", description = "FE gọi endpoint này với token từ link trong email. Token có hiệu lực 24h, chỉ dùng được 1 lần.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Xác thực thành công, có thể đăng nhập ngay"),
                        @ApiResponse(responseCode = "400", description = "Token không hợp lệ hoặc đã hết hạn")
        })
        public ResponseEntity<ResponseBase<Void>> verifyEmail(
                        @Parameter(description = "Token từ link trong email xác thực", required = true) @RequestParam String token) {
                try {
                        accountService.verifyEmail(token);
                        return ResponseBase.success("Xác thực email thành công. Bạn có thể đăng nhập ngay.");
                } catch (RuntimeException e) {
                        return ResponseBase.error(HttpStatus.BAD_REQUEST, e.getMessage());
                }
        }

        @PostMapping("/auth/resend-verification")
        @Operation(summary = "Gửi lại email xác thực", description = "Dùng khi user không nhận được email. Token cũ bị vô hiệu, token mới có hiệu lực 24h.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Email đã được gửi lại"),
                        @ApiResponse(responseCode = "400", description = "Email không tồn tại hoặc đã được xác thực rồi")
        })
        public ResponseEntity<ResponseBase<Void>> resendVerification(@RequestParam String email) {
                try {
                        accountService.resendVerificationEmail(email);
                        return ResponseBase.success("Email xác thực đã được gửi lại.");
                } catch (RuntimeException e) {
                        return ResponseBase.error(HttpStatus.BAD_REQUEST, e.getMessage());
                }
        }

        @PostMapping("/auth/refresh")
        @Operation(summary = "Làm mới access token", description = "Dùng refreshToken để lấy accessToken mới mà không cần đăng nhập lại. refreshToken không đổi.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Trả về accessToken mới"),
                        @ApiResponse(responseCode = "401", description = "refreshToken không hợp lệ hoặc đã hết hạn (30 ngày)")
        })
        public ResponseEntity<ResponseBase<Map<String, Object>>> refreshToken(@RequestParam String refreshToken) {
                try {
                        return ResponseBase.ok(accountService.refreshAccessToken(refreshToken),
                                        "Access token đã được làm mới");
                } catch (RuntimeException e) {
                        return ResponseBase.error(HttpStatus.UNAUTHORIZED, e.getMessage());
                }
        }

        @PostMapping("/auth/logout")
        @Operation(summary = "Đăng xuất", description = "Blacklist accessToken trên Redis + revoke refreshToken. " +
                        "Client xóa cả 2 token khỏi storage sau khi gọi.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Đăng xuất thành công"),
                        @ApiResponse(responseCode = "400", description = "Authorization header không hợp lệ")
        })
        public ResponseEntity<ResponseBase<Void>> logout(
                        @RequestHeader("Authorization") String authHeader,
                        @Parameter(description = "refreshToken cần revoke, gửi nếu có") @RequestParam(required = false) String refreshToken) {
                try {
                        String accessToken = authHeader.substring(7);
                        long remainingMillis = jwtTokenProvider.extractExpiration(accessToken).getTime()
                                        - System.currentTimeMillis();
                        accountService.logout(accessToken, refreshToken, remainingMillis);
                        return ResponseBase.success("Đăng xuất thành công");
                } catch (RuntimeException e) {
                        return ResponseBase.error(HttpStatus.BAD_REQUEST, e.getMessage());
                }
        }

        @GetMapping("/auth/google")
        @Operation(summary = "Đăng nhập Google", description = "Redirect sang Google OAuth2. Sau khi xác thực xong, " +
                        "backend redirect FE đến /auth/success#accessToken=...&refreshToken=...")
        @ApiResponse(responseCode = "302", description = "Redirect sang Google OAuth2 authorization page")
        public ResponseEntity<Object> initiateGoogleLogin() {
                return ResponseEntity.status(HttpStatus.FOUND)
                                .header("Location", "/oauth2/authorization/google")
                                .build();
        }

        @GetMapping("/auth/provider/{email}")
        @Operation(summary = "Lấy thông tin auth provider", description = "FE dùng để kiểm tra account đang dùng LOCAL hay GOOGLE — "
                        +
                        "quyết định có hiện form đổi mật khẩu không.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Trả về authProvider, hasLocalPassword, emailVerified"),
                        @ApiResponse(responseCode = "404", description = "Không tìm thấy account")
        })
        public ResponseEntity<ResponseBase<Map<String, Object>>> getAccountProvider(@PathVariable String email) {
                return accountService.getAccountByEmail(email)
                                .map(account -> {
                                        Map<String, Object> info = new java.util.HashMap<>();
                                        info.put("email", account.getEmail());
                                        info.put("authProvider", account.getAuthProvider());
                                        info.put("hasLocalPassword", account.getPassword() != null);
                                        info.put("isOAuth2Account", account.getAuthProvider() != AuthProvider.LOCAL);
                                        info.put("emailVerified", account.getEmailVerified());
                                        return ResponseBase.ok(info, "Provider information retrieved");
                                })
                                .orElseGet(() -> ResponseBase.error(HttpStatus.NOT_FOUND, "Account not found"));
        }

        @GetMapping("/auth/can-link")
        @Operation(summary = "Kiểm tra có thể link OAuth2 không", description = "canLink = true nếu email chưa tồn tại hoặc đang là LOCAL (có thể link thêm Google).")
        public ResponseEntity<ResponseBase<Map<String, Object>>> canLinkOAuth2Account(@RequestParam String email) {
                boolean canLink = authenticationService.canCreateOAuth2Account(email);
                return ResponseBase.ok(
                                Map.of("email", email, "canLink", canLink),
                                canLink ? "Email có thể dùng cho OAuth2" : "Email đã được link với provider khác");
        }

        @PostMapping("/auth/forgot-password")
        @Operation(summary = "Quên mật khẩu", description = "Gửi email chứa link đặt lại mật khẩu. Chỉ áp dụng cho tài khoản LOCAL.")
        public ResponseEntity<ResponseBase<Void>> forgotPassword(@RequestBody ForgotPasswordRequestDTO dto) {
                try {
                        accountService.forgotPassword(dto);
                        return ResponseBase.success("Nếu email hợp lệ, hệ thống sẽ gửi liên kết khôi phục.");
                } catch (RuntimeException e) {
                        log.warn("Lỗi Forgot Password (đã bị ẩn với FE): {}", e.getMessage());
                        // Security best practice: Tránh lộ lọt email có tồn tại hay không
                        return ResponseBase.success("Nếu email hợp lệ, hệ thống sẽ gửi liên kết khôi phục.");
                }
        }

        @PostMapping("/auth/reset-password")
        @Operation(summary = "Đặt lại mật khẩu", description = "Cập nhật mật khẩu mới bằng token lấy từ email.")
        public ResponseEntity<ResponseBase<Void>> resetPassword(@RequestBody ResetPasswordRequestDTO dto) {
                try {
                        accountService.resetPassword(dto);
                        return ResponseBase.success("Đặt lại mật khẩu thành công. Bạn có thể đăng nhập ngay.");
                } catch (RuntimeException e) {
                        return ResponseBase.error(HttpStatus.BAD_REQUEST, e.getMessage());
                }
        }

        // =========================================================
        // Account CRUD — protected, cần JWT
        // =========================================================

        @GetMapping("/list")
        @Operation(summary = "Lấy danh sách tất cả accounts")
        public ResponseEntity<ResponseBase<List<Account>>> getAllAccounts() {
                return ResponseBase.ok(accountService.getAllAccounts(), "Accounts retrieved successfully");
        }

        @GetMapping("/{id}")
        @Operation(summary = "Lấy account theo ID")
        @ApiResponse(responseCode = "404", description = "Không tìm thấy account")
        public ResponseEntity<ResponseBase<Account>> getAccountById(@PathVariable Integer id) {
                return accountService.getAccountById(id)
                                .map(a -> ResponseBase.ok(a, "Account retrieved successfully"))
                                .orElseGet(() -> ResponseBase.error(HttpStatus.NOT_FOUND,
                                                "Account not found with id: " + id));
        }

        @GetMapping("/email/{email}")
        @Operation(summary = "Lấy account theo email")
        @ApiResponse(responseCode = "404", description = "Không tìm thấy account")
        public ResponseEntity<ResponseBase<Account>> getAccountByEmail(@PathVariable String email) {
                return accountService.getAccountByEmail(email)
                                .map(a -> ResponseBase.ok(a, "Account retrieved successfully"))
                                .orElseGet(() -> ResponseBase.error(HttpStatus.NOT_FOUND, "Account not found"));
        }

        @PutMapping("/{id}")
        @Operation(summary = "Cập nhật account", description = "Cho phép cập nhật: name, phoneNumber, avatarUrl. "
                        +
                        "Không thể đổi vai trò, email, hay mật khẩu qua endpoint này.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
                        @ApiResponse(responseCode = "404", description = "Không tìm thấy account")
        })
        public ResponseEntity<ResponseBase<Account>> updateAccount(
                        @PathVariable Integer id,
                        @RequestBody UpdateProfileRequestDTO dto) {
                try {
                        return ResponseBase.ok(accountService.updateAccount(id, dto),
                                        "Account updated successfully");
                } catch (RuntimeException e) {
                        return ResponseBase.error(HttpStatus.BAD_REQUEST, e.getMessage());
                }
        }

        @PutMapping("/{id}/password")
        @Operation(summary = "Đổi mật khẩu", description = "Yêu cầu mật khẩu cũ và mới. Chỉ áp dụng cho tài khoản LOCAL.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Đổi mật khẩu thành công"),
                        @ApiResponse(responseCode = "400", description = "Mật khẩu cũ không chính xác hoặc lỗi khác"),
                        @ApiResponse(responseCode = "404", description = "Không tìm thấy account")
        })
        public ResponseEntity<ResponseBase<Void>> changePassword(
                        @PathVariable Integer id,
                        @RequestBody ChangePasswordRequestDTO dto) {
                try {
                        accountService.changePassword(id, dto);
                        return ResponseBase.success("Đổi mật khẩu thành công.");
                } catch (RuntimeException e) {
                        return ResponseBase.error(HttpStatus.BAD_REQUEST, e.getMessage());
                }
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Xóa mềm account", description = "Set isActive = false, không xóa khỏi DB. refreshToken bị revoke ngay lập tức.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Xóa thành công"),
                        @ApiResponse(responseCode = "404", description = "Không tìm thấy account")
        })
        public ResponseEntity<ResponseBase<Void>> deleteAccount(@PathVariable Integer id) {
                try {
                        accountService.deleteAccount(id);
                        return ResponseBase.success("Account deleted successfully");
                } catch (RuntimeException e) {
                        return ResponseBase.error(HttpStatus.NOT_FOUND, e.getMessage());
                }
        }

        @GetMapping("/exists/{email}")
        @Operation(summary = "Kiểm tra email đã tồn tại chưa", description = "FE dùng để validate realtime khi user điền form đăng ký.")
        public ResponseEntity<ResponseBase<Boolean>> checkEmailExists(@PathVariable String email) {
                return ResponseBase.ok(accountService.existsByEmail(email), "Email check completed");
        }
}
