package com.example.PixelMageEcomerceProject.config;

import com.example.PixelMageEcomerceProject.entity.Account;
import com.example.PixelMageEcomerceProject.entity.Role;
import com.example.PixelMageEcomerceProject.repository.AccountRepository;
import com.example.PixelMageEcomerceProject.repository.RoleRepository;
import com.example.PixelMageEcomerceProject.security.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitialized implements CommandLineRunner {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public void run(String... args) throws Exception {

        Role adminRole = new Role();
        adminRole.setRoleName("admin");
        roleRepository.save(adminRole);

        Role userRole = new Role();
        userRole.setRoleName("user");
        roleRepository.save(userRole);

        Role managerRole = new Role();
        managerRole.setRoleName("manager");
        roleRepository.save(managerRole);

        Account adminAccount = new Account();
        adminAccount.setEmail("admin@gmail.com");
        adminAccount.setPassword(authenticationService.encodePassword("adminpassword"));
        adminAccount.setName("Hello");
        adminAccount.setPhoneNumber("1234567890");
        adminAccount.setRole(adminRole);
        accountRepository.save(adminAccount);

        Account userAccount = new Account();
        userAccount.setEmail("user@gmail.com");
        userAccount.setPassword(authenticationService.encodePassword("userpassword"));
        userAccount.setName("Hi");
        userAccount.setPhoneNumber("1234567890");
        userAccount.setRole(userRole);
        accountRepository.save(userAccount);

        Account managerAccount = new Account();
        managerAccount.setEmail("manager@gmail.com");
        managerAccount.setPassword(authenticationService.encodePassword("managerpassword"));
        managerAccount.setName("Hey");
        managerAccount.setPhoneNumber("1234567890");
        managerAccount.setRole(managerRole);
        accountRepository.save(managerAccount);

    }
}
