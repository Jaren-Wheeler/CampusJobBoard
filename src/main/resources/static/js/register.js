// -----------------------------------------------------
// Tab Switching (Login <-> Register)
// -----------------------------------------------------

// Get references to UI elements
const loginTab = document.getElementById("loginTab");
const registerTab = document.getElementById("registerTab");
const loginForm = document.getElementById("loginForm");
const registerForm = document.getElementById("registerForm");
const registerSuccess = document.getElementById("registerSuccess");

// -----------------------------------------------------
// Tab Switching (Login <-> Register)
// -----------------------------------------------------

loginTab.addEventListener("click", () => {

    // Show login form
    loginForm.classList.remove("hidden");
    registerForm.classList.add("hidden");
    registerSuccess.classList.add("hidden");

    // Active login style
    loginTab.classList.add("bg-[#E6EED8]", "text-[#2A3810]", "shadow-sm");
    loginTab.classList.remove("text-gray-600");

    // Inactive register style
    registerTab.classList.remove("bg-[#E6EED8]", "text-[#2A3810]", "shadow-sm");
    registerTab.classList.add("text-gray-600");
});

registerTab.addEventListener("click", () => {

    // Show registration form
    loginForm.classList.add("hidden");
    registerForm.classList.remove("hidden");
    registerSuccess.classList.add("hidden");

    // Active register style
    registerTab.classList.add("bg-[#E6EED8]", "text-[#2A3810]", "shadow-sm");
    registerTab.classList.remove("text-gray-600");

    // Inactive login style
    loginTab.classList.remove("bg-[#E6EED8]", "text-[#2A3810]", "shadow-sm");
    loginTab.classList.add("text-gray-600");
});

// -----------------------------------------------------
// User Registration Handler
// -----------------------------------------------------
document.getElementById("registerBtn").addEventListener("click", async () => {

    // Collect input values
    const fullName = document.getElementById("regName").value.trim();
    const email = document.getElementById("regEmail").value.trim();
    const password = document.getElementById("regPassword").value;
    const confirmPassword = document.getElementById("regConfirmPassword").value;
    const role = document.getElementById("regRole").value;   // Dropdown role selection

    const errorEl = document.getElementById("regError");
    errorEl.classList.add("hidden"); // Reset error visibility

    // -----------------------------------------------------
    // Client-side validation
    // -----------------------------------------------------

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

    // -----------------------------------------------------
    // Send Registration Request to Backend
    // -----------------------------------------------------
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

        // Registration failed (email exists, invalid role, etc)
        if (!response.ok) {
            const errData = await response.json();
            errorEl.textContent = errData.message || "Registration failed.";
            errorEl.classList.remove("hidden");
            return;
        }

        // -----------------------------------------------------
        // Registration Success → Show success message + switch to login
        // -----------------------------------------------------
        registerSuccess.classList.remove("hidden");

        // Automatically switch user to the login tab
        loginTab.click();

    } catch (err) {
        // Network failure or unexpected backend issue
        errorEl.textContent = "Error registering user.";
        errorEl.classList.remove("hidden");
    }
});
