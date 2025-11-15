/**
 *
 * Frontend logic for the SUPER_ADMIN dashboard.
 *
 * Responsibilities:
 *  1. Enforce client-side access control
 *  2. Handle creation of new ADMIN accounts via the API
 *  3. Display success/error messages for the form
 *
 * Note:
 *  Real security is enforced server-side using JWT + @PreAuthorize.
 *  This script only protects the UI layer for a better user experience.
 */

document.addEventListener("DOMContentLoaded", () => {

    /**
       ============================================================
       Element References
       ============================================================
     */
    const saName = document.getElementById("saName");
    const saEmail = document.getElementById("saEmail");
    const saPassword = document.getElementById("saPassword");
    const saCreateBtn = document.getElementById("saCreateBtn");
    const saMessage = document.getElementById("saMessage");

    /**
       ============================================================
       Helper: Display success/error messages under the form
       ============================================================
     */
    function showMessage(text, color = "red") {
        saMessage.textContent = text;
        saMessage.classList.remove("hidden", "text-red-600", "text-green-600");

        if (color === "green") {
            saMessage.classList.add("text-green-600");
        } else {
            saMessage.classList.add("text-red-600");
        }
    }

    /**
       ============================================================
       Client-Side Role Check
       ============================================================
     */
    const role = sessionStorage.getItem("role");
    if (role !== "SUPER_ADMIN") {
        window.location.href = "/login";
        return; // prevent running the rest of the script
    }

    /**
       ============================================================
       Create Admin Handler
       ============================================================
     */
    saCreateBtn.addEventListener("click", async () => {

        const fullName = saName.value.trim();
        const email = saEmail.value.trim();
        const password = saPassword.value;

        // Hide previous message
        saMessage.classList.add("hidden");

        /**
           ----------------------------------------------------------
           Client-side validation
           ----------------------------------------------------------
         */
        if (!fullName || !email || !password) {
            showMessage("All fields are required.");
            return;
        }

        if (password.length < 6) {
            showMessage("Password must be at least 6 characters.");
            return;
        }

        /**
           ----------------------------------------------------------
           Retrieve JWT from sessionStorage
           ----------------------------------------------------------
         */
        const token = sessionStorage.getItem("token");
        if (!token) {
            showMessage("Not authenticated. Please log in again.");
            return;
        }

        /**
           ----------------------------------------------------------
           Send Create-Admin request to backend
           ----------------------------------------------------------
         */
        try {
            const response = await fetch("/api/superadmin/create-admin", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify({ fullName, email, password })
            });

            // Backend validation failure (duplicate email, role limit, etc.)
            if (!response.ok) {
                const text = await response.text();
                showMessage(text || "Failed to create admin.");
                return;
            }

            // Success
            showMessage("Admin successfully created!", "green");

            // Reset form fields
            saName.value = "";
            saEmail.value = "";
            saPassword.value = "";

        } catch (err) {
            showMessage("Network error. Please try again.");
        }
    });

});
