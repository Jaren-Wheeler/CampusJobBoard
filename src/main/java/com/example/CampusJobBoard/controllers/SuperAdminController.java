package com.example.CampusJobBoard.controllers;

import org.springframework.security.core.Authentication;
import com.example.CampusJobBoard.dto.CreateAdminRequest;
import com.example.CampusJobBoard.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.CampusJobBoard.dto.AdminSummaryResponse;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import java.util.HashMap;
import java.util.Map;
import com.example.CampusJobBoard.dto.UpdateSuperAdminRequest;

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
    public ResponseEntity<?> createAdmin(
            @Valid @RequestBody CreateAdminRequest req,
            BindingResult result) {

        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();

            result.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );

            return ResponseEntity.badRequest().body(errors);
        }

        try {
            userService.createAdmin(req);
            return ResponseEntity.ok("Admin created successfully.");
        } catch (IllegalStateException ex) {

            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }


    /**
     * Returns a list of all admins (id, name, email).
     * Used by the Super Admin dashboard table.
     */
    @GetMapping("/admins")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<AdminSummaryResponse>> getAdmins() {
        List<AdminSummaryResponse> admins = userService.getAllAdmins();
        return ResponseEntity.ok(admins);
    }

    /**
     * Returns the current number of admin accounts.
     * Used to display "X / 3 admins used".
     */
    @GetMapping("/admin-count")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Long> getAdminCount() {
        long count = userService.countAdmins();
        return ResponseEntity.ok(count);
    }

    /**
     * Deletes an admin by id, but only if they actually have the ADMIN role.
     */
    @DeleteMapping("/admins/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long id) {
        userService.deleteAdminById(id);
        return ResponseEntity.ok("Admin deleted successfully");
    }

    /**
     * Returns the profile info of the logged-in SUPER_ADMIN.
     * Used to fill the setup page on first login.
     */
    @GetMapping("/profile")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> getSuperAdminProfile(Authentication authentication) {
        return ResponseEntity.ok(
                userService.getSuperAdminProfile(authentication.getName())
        );
    }

    /**
     * Allows the SUPER_ADMIN to update their own profile details.
     * This includes fullName, email, and password.
     */
    @PutMapping("/update-profile")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> updateSuperAdminProfile(
            @Valid @RequestBody UpdateSuperAdminRequest request,
            BindingResult result,
            Authentication authentication) {

        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(err ->
                    errors.put(err.getField(), err.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            // authentication.getName() gives the logged-in user's email
            userService.updateSuperAdminProfile(authentication.getName(), request);
            return ResponseEntity.ok("Profile updated successfully.");
        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }
}
