/**
 * Admin Dashboard Job Moderation Logic
 *
 * Responsibilities:
 *  - Fetch all pending job postings from the backend
 *  - Render them into the admin table
 *  - Allow admins to approve or reject jobs
 *
 * This file assumes:
 *  - The admin is authenticated
 *  - A JWT token is stored in sessionStorage
 */

// Table body where rows will be injected
const tableBody = document.getElementById("jobsTableBody");

/**
 * Fetches all pending jobs from the backend.
 * This endpoint is protected and requires ADMIN role.
 */
async function loadPendingJobs() {
    try {
        const res = await fetch("/api/admin/jobs/pending", {
            headers: {
                Authorization: "Bearer " + sessionStorage.getItem("token")
            }
        });

        const jobs = await res.json();
        renderJobs(jobs);

    } catch (err) {
        console.error("Failed to load jobs:", err);
        tableBody.innerHTML = `
            <tr>
                <td colspan="5" class="py-4 text-red-600">
                    Failed to load job postings.
                </td>
            </tr>
        `;
    }
}

/**
 * Renders the list of jobs into the table.
 */
function renderJobs(jobs) {
    tableBody.innerHTML = "";

    // Empty state is valid and expected
    if (jobs.length === 0) {
        tableBody.innerHTML = `
            <tr class="text-gray-400">
                <td class="py-4 px-2" colspan="5">
                    No pending job postings available.
                </td>
            </tr>
        `;
        return;
    }

    jobs.forEach(job => {
        const row = document.createElement("tr");
        row.classList.add("border-b", "text-sm");

        row.innerHTML = `
            <td class="py-3 px-2">${job.jobTitle}</td>
            <td class="py-3 px-2">${job.user?.fullName ?? "Unknown"}</td>
            <td class="py-3 px-2">${job.category}</td>
            <td class="py-3 px-2 font-medium text-yellow-700">
                ${job.status}
            </td>
            <td class="py-3 px-2 space-x-2">
                <button
                    class="px-3 py-1 bg-green-600 text-white rounded hover:bg-green-700"
                    onclick="approveJob(${job.jobId})">
                    Approve
                </button>

                <button
                    class="px-3 py-1 bg-red-600 text-white rounded hover:bg-red-700"
                    onclick="rejectJob(${job.jobId})">
                    Reject
                </button>
            </td>
        `;

        tableBody.appendChild(row);
    });
}

/**
 * Approves a job posting.
 */
async function approveJob(jobId) {
    await fetch(`/api/admin/jobs/${jobId}/approve`, {
        method: "PUT",
        headers: {
            Authorization: "Bearer " + sessionStorage.getItem("token")
        }
    });

    // Refresh table after action
    loadPendingJobs();
}

/**
 * Rejects a job posting.
 */
async function rejectJob(jobId) {
    await fetch(`/api/admin/jobs/${jobId}/reject`, {
        method: "PUT",
        headers: {
            Authorization: "Bearer " + sessionStorage.getItem("token")
        }
    });

    // Refresh table after action
    loadPendingJobs();
}

// Initial load
loadPendingJobs();

window.approveJob = approveJob;
window.rejectJob = rejectJob;
