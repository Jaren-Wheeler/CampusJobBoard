/**
 * Login / Register page logic.
 *
 * Handles:
 *  - Tab switching between Login and Register
 *  - Field-level validation using shared utilities
 *  - Submitting a registration request
 *  - Showing backend errors cleanly
 */

import {
    validateFullName,
    validateEmail,
    validatePassword,
    showFieldError,
    clearErrors
} from "./_shared/index.js";

/* ============================================================
   ELEMENT REFERENCES
   ============================================================ */
const loginTab = document.getElementById("loginTab");
const registerTab = document.getElementById("registerTab");
const loginForm = document.getElementById("loginForm");
const registerForm = document.getElementById("registerForm");
const registerSuccess = document.getElementById("registerSuccess");

const errorGlobal = document.getElementById("regError");

/* ============================================================
   TAB SWITCHING
   ============================================================ */

loginTab.addEventListener("click", () => {
    loginForm.classList.remove("hidden");
    registerForm.classList.add("hidden");
    registerSuccess.classList.add("hidden");

    loginTab.classList.add("bg-[#E6EED8]", "text-[#2A3810]", "shadow-sm");
    loginTab.classList.remove("text-gray-600");

    registerTab.classList.remove("bg-[#E6EED8]", "text-[#2A3810]", "shadow-sm");
    registerTab.classList.add("text-gray-600");
});

registerTab.addEventListener("click", () => {
    loginForm.classList.add("hidden");
    registerForm.classList.remove("hidden");
    registerSuccess.classList.add("hidden");

    registerTab.classList.add("bg-[#E6EED8]", "text-[#2A3810]", "shadow-sm");
    registerTab.classList.remove("text-gray-600");

    loginTab.classList.remove("bg-[#E6EED8]", "text-[#2A3810]", "shadow-sm");
    loginTab.classList.add("text-gray-600");
});

/* ============================================================
   REGISTRATION SUBMIT HANDLER
   ============================================================ */

document.getElementById("registerBtn").addEventListener("click", async () => {

    errorGlobal.classList.add("hidden");
    errorGlobal.textContent = "";

    const fullName = document.getElementById("regName").value.trim();
    const email = document.getElementById("regEmail").value.trim();
    const role = document.getElementById("regRole").value;
    const password = document.getElementById("regPassword").value.trim();
    const confirmPassword = document.getElementById("regConfirmPassword").value.trim();

    // Clear old field errors
    clearErrors([
        "errRegName",
        "errRegEmail",
        "errRegRole",
        "errRegPassword",
        "errRegConfirmPassword"
    ]);

    let hasError = false;

    /* ----------------------------
       Client-side validation
       ---------------------------- */

    const fullNameErr = validateFullName(fullName);
    if (fullNameErr) {
        showFieldError("errRegName", fullNameErr);
        hasError = true;
    }

    const emailErr = validateEmail(email);
    if (emailErr) {
        showFieldError("errRegEmail", emailErr);
        hasError = true;
    }

    if (!role) {
        showFieldError("errRegRole", "Please select a role.");
        hasError = true;
    }

    const pwErr = validatePassword(password);
    if (pwErr) {
        showFieldError("errRegPassword", pwErr);
        hasError = true;
    }

    if (!confirmPassword) {
        showFieldError("errRegConfirmPassword", "Confirm your password.");
        hasError = true;
    } else if (password !== confirmPassword) {
        showFieldError("errRegConfirmPassword", "Passwords do not match.");
        hasError = true;
    }

    if (hasError) return;

    /* ----------------------------
       Submit to backend
       ---------------------------- */

    try {
        const response = await fetch("/api/auth/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ fullName, email, password, role })
        });

        if (!response.ok) {
            const backendErr = await response.json();

            if (backendErr.fullName)
                showFieldError("errRegName", backendErr.fullName);
            if (backendErr.email)
                showFieldError("errRegEmail", backendErr.email);
            if (backendErr.password)
                showFieldError("errRegPassword", backendErr.password);

            // General backend error message
            if (backendErr.message) {
                errorGlobal.textContent = backendErr.message;
                errorGlobal.classList.remove("hidden");
            }

            return;
        }

        registerSuccess.classList.remove("hidden");
        loginTab.click();

    } catch (err) {
        console.error("Network error:", err);
        errorGlobal.textContent = "Network error.";
        errorGlobal.classList.remove("hidden");
    }
});
