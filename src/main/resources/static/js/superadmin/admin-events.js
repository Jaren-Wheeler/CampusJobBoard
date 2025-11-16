import { deleteAdminRequest } from "./index.js";

/**
 * Attaches click handlers to all Delete Admin buttons in the UI.
 * Ensures handlers are not duplicated after a table reload by cloning
 * each button before binding its event listener.
 *
 * @param {string} token - JWT for authentication.
 * @param {Function} reloadCallback - Function to refresh the admin list after deletion.
 */
export function attachDeleteAdminHandlers(token, reloadCallback) {

    if (typeof reloadCallback !== "function") {
        console.error("reloadCallback must be a function");
        return;
    }

    const buttons = document.querySelectorAll(".deleteAdminBtn");

    buttons.forEach(btn => {

        // Replace button with a cloned version to remove any old listeners
        // This prevents stacking multiple click handlers after each reload.
        const newBtn = btn.cloneNode(true);
        btn.replaceWith(newBtn);

        newBtn.addEventListener("click", async () => {
            const id = newBtn.dataset.id;

            if (!confirm("Are you sure you want to delete this admin?")) return;

            const res = await deleteAdminRequest(id, token);

            if (!res.ok) {
                alert("Failed to delete admin.");
                return;
            }

            await reloadCallback();
        });
    });
}
