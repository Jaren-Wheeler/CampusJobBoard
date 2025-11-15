package com.example.CampusJobBoard.controllers;

import com.example.CampusJobBoard.dto.CreateAdminRequest;
import com.example.CampusJobBoard.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for system-level operations available only to SUPER_ADMIN users.
 *
 * <p>This controller exposes endpoints that allow the SUPER_ADMIN to
 * manage administrator accounts and other high-privilege system operations.
 *
 */
@RestController
@RequestMapping("/api/superadmin")
public class SuperAdminController {

    private final UserService userService;

    /**
     * Constructor injection for required services.
     *
     * @param userService Service layer handling admin creation logic.
     */
    public SuperAdminController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Creates a new ADMIN user.
     *
     * <p>This endpoint can only be accessed by a SUPER_ADMIN, and the JWT
     * must contain the proper role. The request contains only the required
     * information for creating an ADMIN user, and the UserService handles
     * validation, hashing, and persistence.
     *
     * @param req DTO containing fullName, email, and password for the new ADMIN.
     * @return 200 OK if the admin account is created successfully.
     */
    @PostMapping("/create-admin")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> createAdmin(@RequestBody CreateAdminRequest req) {
        userService.createAdmin(req);
        return ResponseEntity.ok("Admin created successfully");
    }
}
