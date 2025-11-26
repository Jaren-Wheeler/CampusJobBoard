/**
 * Login page logic for all user roles.
 *
 * Handles:
 *  - Field-level validation
 *  - Sending login requests to the backend
 *  - Storing session data (token, role)
 *  - Redirecting users based on their role
 */

/* /*import {
    validateEmail,
    validatePassword,
    showFieldError,
    clearErrors
} from "./_shared/index.js"

document.getElementById("loginBtn").addEventListener("click", async () => {

    // Inputs
    const email = document.getElementById("loginEmail").value.trim();
    const password = document.getElementById("loginPassword").value.trim();

    // Field error elements
    const errorIds = ["errLoginEmail", "errLoginPassword"];

    // Clear old errors
    clearErrors(errorIds);

    // Hide global backend error
    const errorEl = document.getElementById("loginError");
    errorEl.classList.add("hidden");
    errorEl.textContent = "";

    let hasError = false;

    /**
     * ===========================
     * Field-level validation
     * ===========================
     */

    /*const emailErr = validateEmail(email);
    if (emailErr) {
        showFieldError("errLoginEmail", emailErr);
        hasError = true;
    }

    const pwErr = validatePassword(password);
    if (pwErr) {
        showFieldError("errLoginPassword", pwErr);
        hasError = true;
    }

    if (hasError) return;*/

    /**
     * ===========================
     * Send request to backend
     * ===========================
     */

   /* try {
        const response = await fetch("/api/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        });

        if (!response.ok) {
            // Failed login: show global error only
            errorEl.textContent = "Invalid email or password.";
            errorEl.classList.remove("hidden");
            return;
        }

        /**
         * ===========================
         * Success
         * ===========================
         */
      /*  const data = await response.json();
        const { token, role, mustUpdateProfile } = data; */

        //sessionStorage.setItem("token", token);
        //sessionStorage.setItem("role", role);
        //sessionStorage.setItem("fullName", email);

        // Redirect by role
        /*switch (role) {
            case "ADMIN":
                window.location.href = "/admin/dashboard";
                break;
            case "EMPLOYER":
                window.location.href = "/employer/dashboard";
                break;
            case "STUDENT":
                window.location.href = "/student/dashboard";
                break;
            case "SUPER_ADMIN":
                window.location.href = mustUpdateProfile
                    ? "/superadmin/setup"
                    : "/superadmin/dashboard";
                break;
            default:
                console.error("Unknown role:", role);
        }

    } catch (err) {
        console.error("Network error:", err);
        errorEl.textContent = "Login error. Please try again.";
        errorEl.classList.remove("hidden");
    }
}); */

/**
 * Login page logic (client-side validation ONLY)
 */

import {
    validateEmail,
    validatePassword,
    showFieldError,
    clearErrors
} from "./_shared/index.js";

// When user clicks login button
document.getElementById("loginBtn").addEventListener("click", (e) => {
    const email = document.getElementById("loginEmail").value.trim();
    const password = document.getElementById("loginPassword").value.trim();

    // Clear errors
    clearErrors(["errLoginEmail", "errLoginPassword"]);
    const errorEl = document.getElementById("loginError");
    errorEl.classList.add("hidden");
    errorEl.textContent = "";

    let hasError = false;

    // VALIDATION
    const emailErr = validateEmail(email);
    if (emailErr) {
        showFieldError("errLoginEmail", emailErr);
        hasError = true;
    }

    const pwErr = validatePassword(password);
    if (pwErr) {
        showFieldError("errLoginPassword", pwErr);
        hasError = true;
    }

    // BLOCK SUBMIT IF INVALID
    if (hasError) {
        e.preventDefault(); // IMPORTANT
        return;
    }

    // Otherwise allow form submit to Spring Security
});

