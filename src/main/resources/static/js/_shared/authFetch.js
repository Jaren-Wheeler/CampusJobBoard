/**
 * A wrapper around fetch() that automatically:
 * - attaches Authorization: Bearer <token>
 * - handles 401 unauthorized by redirecting to log in
 * - parses JSON safely
 *
 * This is used so we don't repeat token logic.
 */

export async function authFetch(url, options = {}) {

    const token = sessionStorage.getItem("token");

    // If no token force login
    if (!token) {
        window.location.href = "/login";
        return;
    }

    // Merge user options with our auth headers
    const finalOptions = {
        ...options,
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,
            ...(options.headers || {})
        }
    };

    const response = await fetch(url, finalOptions);

    // If backend returns 401 → session expired or invalid token
    if (response.status === 401) {
        sessionStorage.clear();
        window.location.href = "/login";
        return;
    }

    return response;
}
