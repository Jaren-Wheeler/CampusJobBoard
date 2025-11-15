/**
 * Handles the login process for all user roles (Admin, Employer, Student, Super Admin).
 * Sends info to the backend authentication API and stores the returned JWT and role
 * for later authorization when calling protected endpoints.
 *
 * The backend determines which role the user belongs to and includes it in the JWT payload.
 */
document.getElementById("loginBtn").addEventListener("click", async () => {

    // Collect user input from the form
    const email = document.getElementById("loginEmail").value.trim();
    const password = document.getElementById("loginPassword").value;

    // Reference to the error message area
    const errorEl = document.getElementById("loginError");
    errorEl.classList.add("hidden"); // hide existing errors

    /**
     * ===========================
     * Frontend validation
     * ===========================
     */
    if (!email || !password) {
        errorEl.textContent = "Please enter both fields.";
        errorEl.classList.remove("hidden");
        return;
    }

    try {
        /**
         * ============================================================
         * Send login request to backend (/api/auth/login)
         * ============================================================
         */
        const response = await fetch("/api/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        });

        // If login fails, backend will return 401 or 403
        if (!response.ok) {
            const body = await response.text();
            console.error("Login failed:", response.status, body);

            errorEl.textContent = "Invalid email or password.";
            errorEl.classList.remove("hidden");
            return;
        }

        // Successfully authenticated → extract returned data
        const data = await response.json();
        const { token, role, fullName } = data;

        /**
         * ============================================================
         * Store JWT + role in sessionStorage
         * ============================================================
         *
         * sessionStorage is used instead of localStorage to reduce risk
         * because the token disappears when the tab/browser closes.
         */
        sessionStorage.setItem("token", token);
        sessionStorage.setItem("role", role);
        sessionStorage.setItem("fullName", fullName);

        /**
         * ============================================================
         * Redirect user to their role-based dashboard
         * ============================================================
         */
        switch (role) {
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
                window.location.href = "/superadmin/dashboard";
                break;
            default:
                console.error("Unknown role received from backend:", role);
        }

    } catch (err) {
        /**
         * Network errors or unexpected exceptions
         */
        console.error("Network error:", err);

        errorEl.textContent = "Login error.";
        errorEl.classList.remove("hidden");
    }
});
