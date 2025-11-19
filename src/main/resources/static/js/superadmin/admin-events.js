import {
    validateFullName,
    validateEmail,
    validatePassword
} from "../_shared/validation.js";

import {
    createAdmin,
    deleteAdmin
} from "./admin-api.js";

import { showMessage } from "./admin-ui.js";


/**
 * Handles clicking the Delete button for any admin.
 *
 * Uses event delegation so it continues working
 * even after the table is re-rendered.
 *
 * After delete:
 *   - refreshes the table
 *   - shows a small success/error message
 */
export function attachDeleteAdminHandlers(reloadCallback) {

    document.addEventListener("click", async (e) => {

        if (!e.target.classList.contains("deleteAdminBtn")) return;

        const id = e.target.dataset.id;

        if (!confirm("Delete this admin?")) return;

        const res = await deleteAdmin(id);

        if (!res.ok) {
            showMessage("Failed to delete admin.", "error");
            return;
        }

        await reloadCallback();
        showMessage("Admin deleted successfully.", "success");
    });
}


/**
 * Handles creating a new admin from within the slide-down form.
 *
 * Steps:
 *   1. Clear any old validation messages
 *   2. Validate all fields on the client
 *   3. Submit to the backend
 *   4. Re-render the table
 *   5. Show a success or error message based on the result
 */
export function attachCreateAdminHandler(reloadCallback) {

    const btn = document.getElementById("saCreateAdminBtn");
    if (!btn) return;

    btn.addEventListener("click", async () => {

        ["errSaName", "errSaEmail", "errSaPassword"].forEach(id => {
            const el = document.getElementById(id);
            el.textContent = "";
            el.classList.add("hidden");
        });

        const fullName = document.getElementById("saName").value.trim();
        const email = document.getElementById("saEmail").value.trim();
        const password = document.getElementById("saPassword").value.trim();

        let hasError = false;

        const nameErr = validateFullName(fullName);
        if (nameErr) {
            const el = document.getElementById("errSaName");
            el.textContent = nameErr;
            el.classList.remove("hidden");
            hasError = true;
        }

        const emailErr = validateEmail(email);
        if (emailErr) {
            const el = document.getElementById("errSaEmail");
            el.textContent = emailErr;
            el.classList.remove("hidden");
            hasError = true;
        }

        const pwErr = validatePassword(password);
        if (pwErr) {
            const el = document.getElementById("errSaPassword");
            el.textContent = pwErr;
            el.classList.remove("hidden");
            hasError = true;
        }

        if (hasError) return;

        const res = await createAdmin({ fullName, email, password });

        if (!res.ok) {
            const backendErr = await res.json();
            showMessage(backendErr.error || "Failed to create admin.", "error");
            return;
        }

        await reloadCallback();
        showMessage("Admin created successfully!", "success");
    });
}


/**
 * Opens or closes the Create Admin panel.
 *
 * The panel stays entirely controlled through JS,
 * so this makes the UI feel responsive without page reloads.
 */
export function attachCreateAdminPanelToggle() {
    const openBtn = document.getElementById("openCreateAdmin");
    const cancelBtn = document.getElementById("cancelCreateAdmin");
    const panel = document.getElementById("createAdminPanelWrapper");

    if (!openBtn || !cancelBtn || !panel) return;

    openBtn.addEventListener("click", () => {
        panel.classList.remove("hidden");
        showMessage(""); // Clear messages when opening
    });

    cancelBtn.addEventListener("click", () => {
        panel.classList.add("hidden");
        showMessage(""); // Clear messages on close
    });
}


/**
 * Logs out the current user.
 *
 * Clears sessionStorage and shows a small confirmation
 * before redirecting back to the login page.
 */
export function attachLogoutHandler() {
    const btn = document.getElementById("logoutBtn");
    const msg = document.getElementById("logoutMessage");

    if (!btn) return;

    btn.addEventListener("click", () => {
        sessionStorage.clear();

        if (msg) {
            msg.classList.remove("hidden");
            msg.textContent = "Logging out...";
        }

        setTimeout(() => {
            window.location.href = "/login";
        }, 350);
    });
}
