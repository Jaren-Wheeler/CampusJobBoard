/**
 * Main entry script for the Super Admin dashboard.
 *
 * Handles:
 *  - verifying the user is actually a SUPER_ADMIN
 *  - loading and refreshing the admin list
 *  - wiring up all button actions (create, delete, logout)
 */

import { enforceSuperAdminAccess } from "./access-control.js";

import { fetchAdmins } from "./admin-api.js";
import { renderAdminTable, updateAdminCount } from "./admin-ui.js";

import {
    attachDeleteAdminHandlers,
    attachCreateAdminHandler,
    attachCreateAdminPanelToggle,
    attachLogoutHandler
} from "./admin-events.js";

document.addEventListener("DOMContentLoaded", async () => {

    enforceSuperAdminAccess();

    if (!document.getElementById("adminTable")) return;

    /**
     * Refreshes the dashboard by fetching the latest admin list,
     * re-rendering the table, and updating the count at the top.
     */
    async function reload() {
        const admins = await fetchAdmins();
        renderAdminTable(admins);
        updateAdminCount(admins);
    }

    await reload();

    attachDeleteAdminHandlers(reload);
    attachCreateAdminHandler(reload);
    attachCreateAdminPanelToggle();
    attachLogoutHandler();
});
