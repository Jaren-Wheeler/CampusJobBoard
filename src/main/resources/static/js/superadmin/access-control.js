/**
 * Ensures only authenticated Super Admins can access the dashboard.
 * Redirects users back to the login page if:
 *  - no JWT token is stored, or
 *  - the stored role is not SUPER_ADMIN.
 *
 * @returns {string|null} the JWT token if valid, otherwise null.
 */
export function enforceSuperAdminAccess() {
    const role = sessionStorage.getItem("role");
    const token = sessionStorage.getItem("token");

    // Blocks access if the user is not Super Admin
    if (!token || role !== "SUPER_ADMIN") {
        window.location.href = "/login";
        return null;
    }

    return token;
}
