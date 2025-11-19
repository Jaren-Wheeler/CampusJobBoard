package com.example.CampusJobBoard.config;

import com.example.CampusJobBoard.entities.User;
import com.example.CampusJobBoard.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Initializes the system with a default SUPER_ADMIN account on application startup.
 *
 * <p>This ensures there is always one system-level administrator
 * capable of managing ADMIN accounts and performing global system operations.
 *
 */
@Component
public class SuperAdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor-based dependency injection for required services.
     *
     * @param userRepository Repository for accessing and modifying User records.
     * @param passwordEncoder Encoder used to securely hash the SUPER_ADMIN password.
     */
    public SuperAdminInitializer(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Executed automatically when the application starts.
     *
     * <p>If no SUPER_ADMIN account exists, this method creates one with a default
     * email and password.
     *
     */
    @Override
    public void run(String... args) {
        if (userRepository.countByRole(User.Role.SUPER_ADMIN) == 0) {

            User superAdmin = new User();
            superAdmin.setFullName("System Owner");
            superAdmin.setEmail("superadmin@system.com");
            superAdmin.setPassword(passwordEncoder.encode("SuperAdmin123!"));
            superAdmin.setRole(User.Role.SUPER_ADMIN);

            // REQUIRED — without this setup will never trigger
            superAdmin.setMustUpdateProfile(true);

            userRepository.save(superAdmin);

            System.out.println("\n*** SUPER ADMIN CREATED ***");
            System.out.println("Email: superadmin@system.com");
            System.out.println("Password: SuperAdmin123!");
            System.out.println("****************************\n");
        }
    }
}
