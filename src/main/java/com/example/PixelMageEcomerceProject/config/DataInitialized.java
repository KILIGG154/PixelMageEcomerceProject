package com.example.PixelMageEcomerceProject.config;

import com.example.PixelMageEcomerceProject.entity.Account;
import com.example.PixelMageEcomerceProject.entity.AuthProvider;
import com.example.PixelMageEcomerceProject.entity.Role;
import com.example.PixelMageEcomerceProject.repository.AccountRepository;
import com.example.PixelMageEcomerceProject.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitialized implements CommandLineRunner {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;  // Use PasswordEncoder directly instead of AuthenticationService

    @Override
    public void run(String... args) throws Exception {

        // Create roles including 'customer' role for OAuth2 users
        Role adminRole = new Role();
        adminRole.setRoleName("admin");
        roleRepository.save(adminRole);

        Role userRole = new Role();
        userRole.setRoleName("user");
        roleRepository.save(userRole);

        Role managerRole = new Role();
        managerRole.setRoleName("manager");
        roleRepository.save(managerRole);

        // Add customer role for OAuth2 users
        Role customerRole = new Role();
        customerRole.setRoleName("customer");
        roleRepository.save(customerRole);

        // Create default accounts
        Account adminAccount = new Account();
        adminAccount.setEmail("admin@gmail.com");
        adminAccount.setPassword(passwordEncoder.encode("adminpassword"));  // Use passwordEncoder directly
        adminAccount.setName("Admin User");
        adminAccount.setPhoneNumber("1234567890");
        adminAccount.setAuthProvider(AuthProvider.LOCAL);  // Set auth provider
        adminAccount.setRole(adminRole);
        accountRepository.save(adminAccount);

        Account userAccount = new Account();
        userAccount.setEmail("user@gmail.com");
        userAccount.setPassword(passwordEncoder.encode("userpassword"));
        userAccount.setName("Regular User");
        userAccount.setPhoneNumber("1234567891");
        userAccount.setAuthProvider(AuthProvider.LOCAL);
        userAccount.setRole(userRole);
        accountRepository.save(userAccount);

        Account managerAccount = new Account();
        managerAccount.setEmail("manager@gmail.com");
        managerAccount.setPassword(passwordEncoder.encode("managerpassword"));
        managerAccount.setName("Manager User");
        managerAccount.setPhoneNumber("1234567892");
        managerAccount.setAuthProvider(AuthProvider.LOCAL);
        managerAccount.setRole(managerRole);
        accountRepository.save(managerAccount);

        // Create a test customer account
        Account customerAccount = new Account();
        customerAccount.setEmail("customer@gmail.com");
        customerAccount.setPassword(passwordEncoder.encode("customerpassword"));
        customerAccount.setName("Customer User");
        customerAccount.setPhoneNumber("1234567893");
        customerAccount.setAuthProvider(AuthProvider.LOCAL);
        customerAccount.setRole(customerRole);
        accountRepository.save(customerAccount);

    }
}
