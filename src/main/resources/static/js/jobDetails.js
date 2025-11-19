
// opens the details modal for the specific job clicked
function openModal(id) {
    let modal = document.getElementById("jobModal");
    modal.classList.remove("hidden");
}

//closes the details modal
function closeModal() {
    let modal = document.getElementById("jobModal");
    modal.classList.add("hidden");
}

// opens the modal to create a job application
function openApplyModal() {
    let applyModal = document.getElementById("applyModal");
    applyModal.classList.remove("hidden");
}

// closes both modals at one (create application and details)
function closeApplyModal() {
    let applyModal = document.getElementById("applyModal");
    applyModal.classList.add("hidden");
    closeModal();
}

// go back to details window from apply window
function returnToDetails() {
    let applyModal = document.getElementById("applyModal");
    applyModal.classList.add("hidden");
}