package com.example.PixelMageEcomerceProject.service.interfaces;

import com.example.PixelMageEcomerceProject.dto.request.AccountRequestDTO;
import com.example.PixelMageEcomerceProject.dto.request.LoginRequestDTO;
import com.example.PixelMageEcomerceProject.entity.Account;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AccountService {

    /**
     * Create a new account
     */
    Account createAccount(AccountRequestDTO account);

    /**
     * Update an existing account
     */
    Account updateAccount(Integer customerId, Account account);

    /**
     * Delete an account by ID
     */
    void deleteAccount(Integer customerId);

    /**
     * Get account by ID
     */
    Optional<Account> getAccountById(Integer customerId);

    /**
     * Get account by email
     */
    Optional<Account> getAccountByEmail(String email);

    /**
     * Get all accounts
     */
    List<Account> getAllAccounts();

    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);

    Map<String, Object> loginAccount(LoginRequestDTO loginRequestDTO);
}

