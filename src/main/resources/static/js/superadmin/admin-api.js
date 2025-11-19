import { authFetch } from "../_shared/index.js";

const BASE = "/api/superadmin";


/**
 * Fetches the full list of admins for the dashboard.
 *
 * Uses authFetch so the JWT is included automatically.
 * Returns an empty array if something goes wrong so the UI
 * never breaks while rendering.
 */
export async function fetchAdmins() {
    const res = await authFetch(`${BASE}/admins`, { method: "GET" });

    if (!res.ok) {
        console.error("Failed to load admins");
        return [];
    }

    return res.json();
}

/**
 * Sends a request to create a new admin.
 *
 * The controller handles validation and role assignment.
 * This returns the raw Response object so the caller can
 * check 'res.ok' and show messages accordingly.
 */
export async function createAdmin(adminData) {
    return authFetch(`${BASE}/create-admin`, {
        method: "POST",
        body: JSON.stringify(adminData)
    });
}

/**
 * Deletes an admin account by ID.
 *
 * Only SUPER_ADMIN users can perform this, and the backend
 * ensures the target is actually an ADMIN before removing it.
 */
export async function deleteAdmin(id) {
    return authFetch(`${BASE}/admins/${id}`, {
        method: "DELETE"
    });
}




