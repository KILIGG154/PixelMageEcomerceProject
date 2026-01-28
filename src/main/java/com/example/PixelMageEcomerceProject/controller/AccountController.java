package com.example.PixelMageEcomerceProject.controller;

import com.example.PixelMageEcomerceProject.dto.request.AccountRequestDTO;
import com.example.PixelMageEcomerceProject.dto.request.LoginRequestDTO;
import com.example.PixelMageEcomerceProject.dto.response.ResponseBase;
import com.example.PixelMageEcomerceProject.entity.Account;
import com.example.PixelMageEcomerceProject.service.interfaces.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "Account Management", description = "APIs for managing user accounts")
@SecurityRequirement(name = "bearerAuth")
public class AccountController {

    private final AccountService accountService;

    /**
     * Create a new account
     */
    @PostMapping("/registration")
    @Operation(
            summary = "Create a new account",
            description = "Create a new user account with email, password, name, phone number and role"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "400", description = "Bad request - Email already exists or invalid data",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> createAccount(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Account details to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AccountRequestDTO.class))
            )
            @RequestBody AccountRequestDTO account) {
        try {
            Account createdAccount = accountService.createAccount(account);
            ResponseBase response = new ResponseBase(
                HttpStatus.CREATED.value(),
                "Account created successfully",
                createdAccount
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            ResponseBase response = new ResponseBase(
                HttpStatus.BAD_REQUEST.value(),
                "Failed to create account: " + e.getMessage(),
                null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * Get all accounts
     */
    @GetMapping
    @PreAuthorize("hasRole('admin')")
    @Operation(
            summary = "Get all accounts",
            description = "Retrieve a list of all user accounts in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        ResponseBase response = new ResponseBase(
            HttpStatus.OK.value(),
            "Accounts retrieved successfully",
            accounts
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Get account by ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    @Operation(
            summary = "Get account by ID",
            description = "Retrieve account details by customer ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getAccountById(
            @Parameter(description = "Customer ID", required = true)
            @PathVariable Integer id) {
        return accountService.getAccountById(id)
                .map(account -> {
                    ResponseBase response = new ResponseBase(
                        HttpStatus.OK.value(),
                        "Account retrieved successfully",
                        account
                    );
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    ResponseBase response = new ResponseBase(
                        HttpStatus.NOT_FOUND.value(),
                        "Account not found with id: " + id,
                        null
                    );
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    /**
     * Get account by email
     */
    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('admin') or hasRole('staff')")
    @Operation(
            summary = "Get account by email",
            description = "Retrieve account details by email address"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> getAccountByEmail(
            @Parameter(description = "Email address", required = true)
            @PathVariable String email) {
        return accountService.getAccountByEmail(email)
                .map(account -> {
                    ResponseBase response = new ResponseBase(
                        HttpStatus.OK.value(),
                        "Account retrieved successfully",
                        account
                    );
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    ResponseBase response = new ResponseBase(
                        HttpStatus.NOT_FOUND.value(),
                        "Account not found with email: " + email,
                        null
                    );
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    /**
     * Update account
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Update account",
            description = "Update existing account information"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account updated successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid data or email already exists",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> updateAccount(
            @Parameter(description = "Customer ID", required = true)
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated account details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Account.class))
            )
            @RequestBody Account account) {
        try {
            Account updatedAccount = accountService.updateAccount(id, account);
            ResponseBase response = new ResponseBase(
                HttpStatus.OK.value(),
                "Account updated successfully",
                updatedAccount
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ResponseBase response = new ResponseBase(
                HttpStatus.BAD_REQUEST.value(),
                "Failed to update account: " + e.getMessage(),
                null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * Delete account
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete account",
            description = "Delete an account by customer ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Account deleted successfully",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class))),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> deleteAccount(
            @Parameter(description = "Customer ID", required = true)
            @PathVariable Integer id) {
        try {
            accountService.deleteAccount(id);
            ResponseBase response = new ResponseBase(
                HttpStatus.NO_CONTENT.value(),
                "Account deleted successfully",
                null
            );
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        } catch (RuntimeException e) {
            ResponseBase response = new ResponseBase(
                HttpStatus.NOT_FOUND.value(),
                "Failed to delete account: " + e.getMessage(),
                null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Check if email exists
     */
    @GetMapping("/exists/{email}")
    @Operation(
            summary = "Check if email exists",
            description = "Check if an email address is already registered in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email check completed",
                    content = @Content(schema = @Schema(implementation = ResponseBase.class)))
    })
    public ResponseEntity<ResponseBase> checkEmailExists(
            @Parameter(description = "Email address to check", required = true)
            @PathVariable String email) {
        boolean exists = accountService.existsByEmail(email);
        ResponseBase response = new ResponseBase(
            HttpStatus.OK.value(),
            "Email check completed",
            exists
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseBase> loginAccount(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            Map<String, Object> loginResponse = accountService.loginAccount(loginRequestDTO);
            ResponseBase response = new ResponseBase(
                HttpStatus.OK.value(),
                "Login successful",
                loginResponse
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ResponseBase response = new ResponseBase(
                HttpStatus.UNAUTHORIZED.value(),
                "Login failed: " + e.getMessage(),
                null
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}

