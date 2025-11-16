/**
 * Fetches the full list of admin accounts.
 *
 * @param {string} token - JWT for authentication.
 * @returns {Promise<Array>} list of admin objects.
 * @throws {Error} if the request fails.
 */
export async function fetchAdminList(token) {
    const res = await fetch("/api/superadmin/admins", {
        headers: { "Authorization": `Bearer ${token}` }
    });

    if (!res.ok) throw new Error("Failed to fetch admin list");
    return await res.json();
}


/**
 * Gets the current number of admins in the system.
 * Helps with enforcing max-admin limits on the dashboard.
 *
 * @param {string} token - JWT for authentication.
 * @returns {Promise<number>} the current admin count.
 * @throws {Error} if the request fails.
 */
export async function fetchAdminCount(token) {
    const res = await fetch("/api/superadmin/admin-count", {
        headers: { "Authorization": `Bearer ${token}` }
    });

    if (!res.ok) throw new Error("Failed to fetch admin count");
    return await res.json();
}


/**
 * Sends a request to create a new admin account.
 * Backend handles validation and role assignment.
 *
 * @param {Object} data - { fullName, email, password }.
 * @param {string} token - JWT for authentication.
 * @returns {Promise<Response>} the raw fetch response.
 */
export async function createAdmin(data, token) {
    return await fetch("/api/superadmin/create-admin", {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    });
}


/**
 * Deletes an admin account by id.
 *
 * @param {number|string} id - ID of the admin to delete.
 * @param {string} token - JWT for authentication.
 * @returns {Promise<Response>} fetch response with status.
 */
export async function deleteAdminRequest(id, token) {
    return await fetch(`/api/superadmin/admins/${id}`, {
        method: "DELETE",
        headers: { "Authorization": `Bearer ${token}` }
    });
}
