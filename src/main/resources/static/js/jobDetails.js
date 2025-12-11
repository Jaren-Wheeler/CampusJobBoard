
// opens the details modal for the specific job clicked
function openModal(button) {
    // Read dataset
    const d = button.dataset;

    // Fill modal fields
    document.getElementById("modalJobTitle").innerText = d.title;
    document.getElementById("modalJobDescription").innerText = d.description;

    document.getElementById("modalInfoTitle").innerText = "Job Title: " + d.title;
    document.getElementById("modalInfoCompany").innerText = "Company: " + (d.company || "Unknown");
    document.getElementById("modalInfoCategory").innerText = "Category: " + d.category;
    document.getElementById("modalInfoSalary").innerText = "Salary: $" + d.salary;
    document.getElementById("modalInfoPosted").innerText = "Posted: " + d.created;
    document.getElementById("modalInfoDeadline").innerText = "Deadline: " + d.deadline;

    // Apply button hidden field
    document.getElementById("jobIdInput").value = d.id;

    // Show modal
    document.getElementById("jobModal").classList.remove("hidden");
}

//closes the details modal
function closeModal() {
    let modal = document.getElementById("jobModal");
    modal.classList.add("hidden");
}


