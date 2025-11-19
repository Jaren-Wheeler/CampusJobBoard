/**
 * Handles the Super Admin "first-time setup" page.
 *
 * Responsibilities:
 *  - Load the logged-in SUPER_ADMIN's profile
 *  - Validate any changes they submit
 *  - Send the updated info to the backend
 */

import {
    authFetch,
    validateEmail,
    validateFullName,
    validatePassword
} from "../_shared/index.js";

// --- Form elements ---
const nameEl = document.getElementById("setupName");
const emailEl = document.getElementById("setupEmail");
const passwordEl = document.getElementById("setupPassword");
const messageEl = document.getElementById("saSetupMessage");

// Field-level error elements
const errName = document.getElementById("errName");
const errEmail = document.getElementById("errEmail");
const errPassword = document.getElementById("errPassword");

let originalEmail = null;

/**
 * Hides all field-level errors.
 */
function clearErrors() {
    [errName, errEmail, errPassword].forEach(e => {
        e.textContent = "";
        e.classList.add("hidden");
    });
}

/**
 * Loads the SUPER_ADMIN's current profile information
 * so the form is pre-filled when the page opens.
 */
async function loadProfile() {
    try {
        const res = await authFetch("/api/superadmin/profile");
        const data = await res.json();

        nameEl.value = data.fullName || "";
        emailEl.value = data.email || "";
        originalEmail = data.email;

    } catch (err) {
        console.error("Failed to load profile:", err);
    }
}

void loadProfile();

/**
 * Handles saving the SUPER_ADMIN's updated profile information.
 * Runs full validation first, then sends the update to the backend.
 */
document.getElementById("setupSubmit").addEventListener("click", async () => {

    clearErrors();
    messageEl.classList.add("hidden");

    const fullName = nameEl.value.trim();
    const email = emailEl.value.trim();
    const password = passwordEl.value.trim();

    let hasError = false;

    // --- FULL NAME ---
    const nameErr = validateFullName(fullName);
    if (nameErr) {
        errName.textContent = nameErr;
        errName.classList.remove("hidden");
        hasError = true;
    }

    // --- EMAIL ---
    const emailErr = validateEmail(email);
    if (emailErr) {
        errEmail.textContent = emailErr;
        errEmail.classList.remove("hidden");
        hasError = true;
    }

    // --- PASSWORD ---
    const pwErr = validatePassword(password);
    if (pwErr) {
        errPassword.textContent = pwErr;
        errPassword.classList.remove("hidden");
        hasError = true;
    }

    if (hasError) return;

    const payload = { fullName, email, password };

    try {
        const res = await authFetch("/api/superadmin/update-profile", {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        if (!res.ok) {
            const errors = await res.json();

            // Backend validation
            if (errors.fullName) {
                errName.textContent = errors.fullName;
                errName.classList.remove("hidden");
            }
            if (errors.email) {
                errEmail.textContent = errors.email;
                errEmail.classList.remove("hidden");
            }
            if (errors.password) {
                errPassword.textContent = errors.password;
                errPassword.classList.remove("hidden");
            }

            if (errors.error) {
                messageEl.textContent = errors.error;
                messageEl.classList.remove("hidden");
                messageEl.classList.add("text-red-600");
            }

            return;
        }

        messageEl.textContent = "Profile updated successfully!";
        messageEl.classList.remove("hidden");
        messageEl.classList.add("text-green-600");

        // Redirect back to dashboard
        setTimeout(() => {
            window.location.href = "/superadmin/dashboard";
        }, 800);

    } catch (err) {
        console.error("Error updating profile:", err);
    }
});
