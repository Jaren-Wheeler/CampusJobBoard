// Login button click
document.getElementById("loginBtn").addEventListener("click", async () => {

    // Get email + password field values
    const email = document.getElementById("loginEmail").value.trim();
    const password = document.getElementById("loginPassword").value;

    // Error message element
    const errorEl = document.getElementById("loginError");
    errorEl.classList.add("hidden");

    // frontend validation
    if (!email || !password) {
        errorEl.textContent = "Please enter both fields.";
        errorEl.classList.remove("hidden");
        return;
    }

    try {
        // Send login request to backend authentication API
        const response = await fetch("/api/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        });

        // The JSON response
        const data = await response.json();

        // If backend returns a non-200 response (401, 403, etc.)
        if (!response.ok) {
            throw new Error("Invalid email or password");
        }

        // Extract the returned fields
        const { token, role, fullName } = data;

        // Stores in sessionStorage
        // These will be used to authorize API calls & personalize dashboards
        sessionStorage.setItem("token", token);
        sessionStorage.setItem("role", role);
        sessionStorage.setItem("fullName", fullName);

        // Redirect user to correct dashboard based on their role
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
            default:
                // Unexpected role from backend
                console.error("Unknown role:", role);
        }

    } catch (err) {
        // Show login error message to user
        const errorText = document.getElementById("loginError");
        errorText.textContent = err.message || "Login error.";
        errorText.classList.remove("hidden");
    }
});
