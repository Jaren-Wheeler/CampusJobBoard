import {
    enforceSuperAdminAccess,
    fetchAdminList,
    fetchAdminCount,
    createAdmin,
    renderAdminTable,
    setAdminCount,
    attachDeleteAdminHandlers,
    showCreatePanel,
    hideCreatePanel,
    showMessage,
    clearCreateAdminFields
} from "./index.js";

document.addEventListener("DOMContentLoaded", async () => {

    // only Super Admins can load this page
    const token = enforceSuperAdminAccess();
    if (!token) return;

    const MAX_ADMINS = 3;

    /**
     * Refreshes all dynamic UI data:
     *  - admin table
     *  - delete button handlers
     *  - admin count header
     *
     * Called on first load and after creating/deleting admins.
     */
    async function reload() {
        const admins = await fetchAdminList(token);
        renderAdminTable(admins);
        attachDeleteAdminHandlers(token, reload);

        const count = await fetchAdminCount(token);
        setAdminCount(count, MAX_ADMINS);
    }

    // -----------------------------------
    // Panel Open / Close Buttons
    // -----------------------------------
    const openBtn = document.getElementById("openCreateAdmin");
    if (openBtn) {
        openBtn.addEventListener("click", showCreatePanel);
    }

    const cancelBtn = document.getElementById("cancelCreateAdmin");
    if (cancelBtn) {
        cancelBtn.addEventListener("click", hideCreatePanel);
    }

    // -----------------------------------
    // Create Admin Form Handler
    // -----------------------------------
    const createBtn = document.getElementById("saCreateBtn");
    if (createBtn) {
        createBtn.addEventListener("click", async () => {

            // Read form values
            const fullName = document.getElementById("saName").value.trim();
            const email = document.getElementById("saEmail").value.trim();
            const password = document.getElementById("saPassword").value;

            // Send to backend
            const res = await createAdmin({ fullName, email, password }, token);

            if (!res.ok) {
                // Show backend error (password, email, duplicate user, etc.)
                const errMsg = await res.text();
                showMessage(errMsg, "error");
                return;
            }

            // Successful creation feedback
            showMessage("Admin created successfully!", "success");
            clearCreateAdminFields(); // keep panel open but reset inputs
            await reload();           // refresh list + admin count
        });
    }

    // -----------------------------------
    // Logout Button
    // -----------------------------------
    const logoutBtn = document.getElementById("logoutBtn");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", () => {
            sessionStorage.removeItem("token");
            sessionStorage.removeItem("role");
            window.location.href = "/login";
        });
    }

    await reload();
});
