/**
 * Validation + small UI helpers shared across the whole app.
 *
 * These functions keep form logic consistent so every page
 * (login, register, admin panel, setup screen) behaves the same.
 */

/* ============================================================
   FIELD VALIDATION RULES
   These return a string when invalid, or null when valid.
   ============================================================ */

export function validatePassword(pw) {
    if (!pw) return "Password is required.";
    if (pw.length < 8) return "Password must be at least 8 characters.";
    if (!/[A-Z]/.test(pw)) return "Password must contain an uppercase letter.";
    if (!/[a-z]/.test(pw)) return "Password must contain a lowercase letter.";
    if (!/[0-9]/.test(pw)) return "Password must contain a number.";
    if (!/[!@#$%^&*()]/.test(pw)) return "Password must contain a special character.";
    return null;
}

export function validateEmail(email) {
    if (!email) return "Email is required.";
    if (!/^[A-Za-z0-9+_.-]+@(.+)$/.test(email)) {
        return "Enter a valid email address.";
    }
    return null;
}

export function validateFullName(name) {
    if (!name) return "Full name is required.";

    // Basic "first and last name" structure
    if (!/^[A-Za-z]+( [A-Za-z]+)+$/.test(name)) {
        return "Enter a valid full name (first and last).";
    }

    if (name.length < 3 || name.length > 50) {
        return "Full name must be between 3 and 50 characters.";
    }

    return null;
}

/* ============================================================
   UI HELPERS — Show and hide field-level validation errors
   ============================================================ */

export function clearFieldError(id) {
    const el = document.getElementById(id);
    if (!el) return;
    el.textContent = "";
    el.classList.add("hidden");
}

export function showFieldError(id, message) {
    const el = document.getElementById(id);
    if (!el) return;
    el.textContent = message;
    el.classList.remove("hidden");
}

export function clearErrors(ids) {
    ids.forEach(id => clearFieldError(id));
}
