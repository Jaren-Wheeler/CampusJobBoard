/**
 * Renders the admin table content based on the provided admin list.
 * Handles empty states and builds rows dynamically, including delete buttons.
 *
 * @param {Array} admins - List of admin objects to display.
 */
export function renderAdminTable(admins) {
    const table = document.getElementById("adminTable");
    if (!table) return;

    if (!admins || admins.length === 0) {
        table.innerHTML = `
            <tr>
                <td colspan="3" class="p-3 text-gray-500 italic">
                    No admin accounts created yet.
                </td>
            </tr>`;
        return;
    }

    table.innerHTML = admins
        .map(admin => `
            <tr class="border-b">
                <td class="p-3">${admin.fullName}</td>
                <td class="p-3">${admin.email}</td>
                <td class="p-3">
                    <button
                        class="deleteAdminBtn text-red-600 hover:underline"
                        data-id="${admin.id}">
                        Delete
                    </button>
                </td>
            </tr>
        `)
        .join("");
}


/**
 * Updates the "Admins: X / 3" counter in the header.
 *
 * @param {number} count - Current number of admins.
 * @param {number} max - Maximum allowed admins.
 */
export function setAdminCount(count, max) {
    const el = document.getElementById("adminCount");
    if (el) el.textContent = `Admins: ${count} / ${max}`;
}


/**
 * Displays the "Create Admin" panel.
 */
export function showCreatePanel() {
    document.getElementById("createAdminPanelWrapper")
        .classList.remove("hidden");
}


/**
 * Hides the "Create Admin" panel and resets all form fields and messages.
 */
export function hideCreatePanel() {
    const wrapper = document.getElementById("createAdminPanelWrapper");
    wrapper.classList.add("hidden");

    document.getElementById("saName").value = "";
    document.getElementById("saEmail").value = "";
    document.getElementById("saPassword").value = "";

    const msg = document.getElementById("saMessage");
    msg.classList.add("hidden");
    msg.textContent = "";
}


/**
 * Clears only the input fields in the Create Admin form.
 * Used after successful creation to keep the panel open.
 */
export function clearCreateAdminFields() {
    document.getElementById("saName").value = "";
    document.getElementById("saEmail").value = "";
    document.getElementById("saPassword").value = "";
}


/**
 * Displays a feedback message inside the Create Admin panel.
 *
 * @param {string} text - Message to show.
 * @param {"success"|"error"} [type="success"] - Determines text color.
 */
export function showMessage(text, type = "success") {
    const msg = document.getElementById("saMessage");

    msg.textContent = text;
    msg.classList.remove("hidden");

    msg.classList.toggle("text-green-600", type === "success");
    msg.classList.toggle("text-red-600", type === "error");
}
