
// opens the details modal for the specific job clicked
function openModal(id) {

    let modal = document.getElementById("jobModal");
    modal.classList.remove("hidden");

    document.getElementById("jobIdInput").value = id;
}

//closes the details modal
function closeModal() {
    let modal = document.getElementById("jobModal");
    modal.classList.add("hidden");
}


// confirmation of application
function applicationSubmit() {

}