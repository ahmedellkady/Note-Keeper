document.querySelector(".new-note").addEventListener("click", () => {
    document.getElementById("newNoteModal").classList.remove("hidden");
});

function closeModal() {
    document.getElementById("newNoteModal").classList.add("hidden");
}

function createNote() {
    const title = document.getElementById("noteTitle").value.trim();
    const checkedTags = Array.from(document.querySelectorAll(".tags input:checked")).map(tag => tag.value);

    if (!title) {
        alert("Please enter a note title.");
        return;
    }

    localStorage.setItem("newNote", JSON.stringify({ title, tags: checkedTags }));

    window.location.href = "editor.html";
}

document.querySelectorAll(".history").forEach(btn => {
    btn.addEventListener("click", () => {
        const panel = document.getElementById("versionPanel");
        panel.classList.toggle("hidden");
    });
});

document.querySelectorAll(".edit").forEach(btn => {
    btn.addEventListener("click", () => {
        document.getElementById("shareModal").classList.remove("hidden");
    });
});

function closeShareModal() {
    document.getElementById("shareModal").classList.add("hidden");
}

function shareNote() {
    const user = document.getElementById("shareWith").value.trim();
    const permission = document.getElementById("permission").value;

    if (!user) {
        alert("Please enter a username or email.");
        return;
    }

    console.log(`Note shared with ${user} as ${permission}`);

    closeShareModal();
}
