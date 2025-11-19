/**
 * Handles the Login/Register tab switching and the full registration flow
 * for public-facing users (Student, Employer, Admin).
 *
 */

/**
   ============================================================
   Element References
   ============================================================
 */
const loginTab = document.getElementById("loginTab");
const registerTab = document.getElementById("registerTab");
const loginForm = document.getElementById("loginForm");
const registerForm = document.getElementById("registerForm");
const registerSuccess = document.getElementById("registerSuccess");

/* ============================================================
   Tab Switching (Login <-> Register)
   ============================================================ */

/**
 * Switches UI to Login Tab
 */
loginTab.addEventListener("click", () => {
    // Display login form and hide registration content
    loginForm.classList.remove("hidden");
    registerForm.classList.add("hidden");
    registerSuccess.classList.add("hidden");

    // Activate login tab styling
    loginTab.classList.add("bg-[#E6EED8]", "text-[#2A3810]", "shadow-sm");
    loginTab.classList.remove("text-gray-600");

    // Deactivate register tab
    registerTab.classList.remove("bg-[#E6EED8]", "text-[#2A3810]", "shadow-sm");
    registerTab.classList.add("text-gray-600");
});

/**
 * Switches UI to Register Tab
 */
registerTab.addEventListener("click", () => {
    // Display registration form and hide login content
    loginForm.classList.add("hidden");
    registerForm.classList.remove("hidden");
    registerSuccess.classList.add("hidden");

    // Activate register tab styling
    registerTab.classList.add("bg-[#E6EED8]", "text-[#2A3810]", "shadow-sm");
    registerTab.classList.remove("text-gray-600");

    // Deactivate login tab
    loginTab.classList.remove("bg-[#E6EED8]", "text-[#2A3810]", "shadow-sm");
    loginTab.classList.add("text-gray-600");
});

/**
   ============================================================
   User Registration Handler
   ============================================================
 */
document.getElementById("registerBtn").addEventListener("click", async () => {

    // Get form data
    const fullName = document.getElementById("regName").value.trim();
    const email = document.getElementById("regEmail").value.trim();
    const password = document.getElementById("regPassword").value;
    const confirmPassword = document.getElementById("regConfirmPassword").value;
    const role = document.getElementById("regRole").value; // selected user role

    const errorEl = document.getElementById("regError");
    errorEl.classList.add("hidden"); // clear old errors

    /**
       ----------------------------------------------------------
       Client-Side Validation
       ----------------------------------------------------------
     */
    if (!fullName || !email || !password) {
        errorEl.textContent = "Please fill all fields.";
        errorEl.classList.remove("hidden");
        return;
    }

    if (!role) {
        errorEl.textContent = "Please select a role.";
        errorEl.classList.remove("hidden");
        return;
    }

    if (password.length < 6) {
        errorEl.textContent = "Password must be at least 6 characters.";
        errorEl.classList.remove("hidden");
        return;
    }

    if (password !== confirmPassword) {
        errorEl.textContent = "Passwords do not match.";
        errorEl.classList.remove("hidden");
        return;
    }

    /**
       ----------------------------------------------------------
       Submit Registration to Backend
       ----------------------------------------------------------
     */
    try {
        const response = await fetch("/api/auth/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                fullName,
                email,
                password,
                role
            })
        });

        // Backend validation failed (duplicate email, invalid role, etc.)
        if (!response.ok) {
            const errData = await response.json();
            errorEl.textContent = errData.message || "Registration failed.";
            errorEl.classList.remove("hidden");
            return;
        }

        /**
           ----------------------------------------------------------
           Registration Successful
           ----------------------------------------------------------
           Show success message and switch back to Log in tab so the
           user can immediately attempt login with their new account.
        ----------------------------------------------------------
         */
        registerSuccess.classList.remove("hidden");

        // Automatically switch to log in tab for better UX
        loginTab.click();

    } catch (err) {
        // Network-level failure (offline, server unreachable, etc.)
        errorEl.textContent = "Error registering user.";
        errorEl.classList.remove("hidden");
    }
});
