/**
 * Renders the list of admin accounts in the table.
 *
 * This:
 *   - clears any previous rows
 *   - shows the “empty” message if no admins exist
 *   - injects each admin as a row with a delete button
 */
export function renderAdminTable(admins) {
    const tbody = document.getElementById("adminTable");
    const emptyMsg = document.getElementById("adminEmpty");

    if (!tbody) {
        console.warn("renderAdminTable: #adminTable not found");
        return;
    }

    tbody.innerHTML = "";

    if (!admins || admins.length === 0) {
        emptyMsg?.classList.remove("hidden");
        return;
    }

    emptyMsg?.classList.add("hidden");

    admins.forEach(admin => {
        const tr = document.createElement("tr");

        tr.innerHTML = `
            <td class="px-4 py-2 border-b">${admin.fullName}</td>
            <td class="px-4 py-2 border-b">${admin.email}</td>
            <td class="px-4 py-2 border-b text-right">
                <button
                    class="deleteAdminBtn text-red-600 hover:underline"
                    data-id="${admin.userId}">
                    Delete
                </button>
            </td>
        `;

        tbody.appendChild(tr);
    });
}


/**
 * Updates the “Admins: X / 3” text shown in the header bar.
 * Called every time the list is reloaded so the count is always accurate.
 */
export function updateAdminCount(admins) {
    const el = document.getElementById("adminCount");
    if (!el) return;

    const count = admins?.length ?? 0;
    const max = 3; // System limit for admins

    el.textContent = `Admins: ${count} / ${max}`;
}

/**
 * Shows a temporary success/error message inside the Create Admin panel.
 * The element already exists this just toggles visibility
 * and applies the appropriate color.
 */
export function showMessage(text, type = "success") {
    const el = document.getElementById("saMessage");
    if (!el) return;

    el.classList.remove("hidden", "text-red-600", "text-green-600");

    if (!text) {
        el.textContent = "";
        el.classList.add("hidden");
        return;
    }

    el.textContent = text;

    if (type === "error") {
        el.classList.add("text-red-600");
    } else {
        el.classList.add("text-green-600");
    }
}
