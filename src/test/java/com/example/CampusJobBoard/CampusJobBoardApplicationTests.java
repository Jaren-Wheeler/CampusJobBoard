package com.example.CampusJobBoard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the Campus Job Board application.
 *
 * This test suite verifies:
 *  - The Spring Boot application context loads correctly
 *  - Publicly accessible routes are reachable without authentication
 *  - Protected routes are secured and redirect unauthenticated users
 *
 * These tests are intentionally lightweight and focus on
 * routing + security configuration rather than business logic.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ApplicationIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    // -------------------------------------------------------------------------
    // Application Context Test
    // -------------------------------------------------------------------------

    /**
     * Verifies that the Spring Boot application context loads successfully.
     *
     * If this test fails, it indicates a configuration, dependency,
     * or bean initialization problem.
     */
    @Test
    void contextLoads() {
        // Context load failure will cause this test to fail automatically
    }

    // -------------------------------------------------------------------------
    // Public Route Tests
    // -------------------------------------------------------------------------

    /**
     * Verifies that the login page is publicly accessible.
     *
     * This endpoint must be reachable without authentication
     * in order for users to sign in.
     */
    @Test
    void loginPageLoadsSuccessfully() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    // -------------------------------------------------------------------------
    // Security Access Tests (Unauthenticated)
    // -------------------------------------------------------------------------

    /**
     * Ensures that the student dashboard cannot be accessed
     * without an authenticated session.
     *
     * Expected behavior: redirect to login page.
     */
    @Test
    void studentDashboardRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/student/dashboard"))
                .andExpect(status().is3xxRedirection());
    }

    /**
     * Ensures that the admin dashboard cannot be accessed
     * without authentication.
     *
     * Expected behavior: redirect to login page.
     */
    @Test
    void adminDashboardRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().is3xxRedirection());
    }

    /**
     * Ensures that the super admin dashboard cannot be accessed
     * without authentication.
     *
     * This test verifies that the highest-privilege route
     * is properly secured by Spring Security.
     */
    @Test
    void superAdminDashboardRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/superadmin/dashboard"))
                .andExpect(status().is3xxRedirection());
    }
}
