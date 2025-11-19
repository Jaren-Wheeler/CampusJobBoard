/**
 * Ensures only authenticated Super Admins can access this page.
 * Redirects to /login if:
 *   - no JWT exists, or
 *   - role is not SUPER_ADMIN.
 */
export function enforceSuperAdminAccess() {
    const token = sessionStorage.getItem("token");
    const role = sessionStorage.getItem("role");

    if (!token || role !== "SUPER_ADMIN") {
        window.location.href = "/login";
    }
}
