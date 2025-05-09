import { getUserNotes, getNoteVersions, restoreNoteVersion, addNote, shareNote as shareNoteApi } from '../api/notesApi.js';

let currentNoteToShare = null;
let searchInput;
let tagFilter;

document.addEventListener("DOMContentLoaded", async () => {
    const user = JSON.parse(localStorage.getItem("user"));
    const tableBody = document.querySelector(".notes-table tbody");

    searchInput = document.querySelector(".search");
    tagFilter = document.getElementById("tagFilter");

    searchInput.addEventListener("input", filterNotes);
    tagFilter.addEventListener("change", filterNotes);

    if (!user) {
        alert("Not logged in. Redirecting to login...");
        window.location.href = "auth.html";
        return;
    }

    try {
        const notes = await getUserNotes(user.id);
        tableBody.innerHTML = "";

        notes.forEach(note => {
            const tr = document.createElement("tr");
            tr.dataset.tag = note.tag;
            tr.innerHTML = `
        <td class="clickable-title"><b>${note.title}</b></td>
        <td>${note.sharedWithName}</td>
        <td>${new Date(note.updatedAt).toLocaleString()}</td>
        <td>${note.tag}</td>
        <td>${note.permission}</td>
        <td>
          <button class="edit"><i class="fas fa-pen"></i></button>
          <button class="history"><i class="fas fa-clock-rotate-left"></i></button>
        </td>
      `;

            tr.querySelector(".history").addEventListener("click", () => showHistory(note.id, note.title));

            tr.querySelector(".edit").addEventListener("click", () => {
                currentNoteToShare = note.id;
                document.getElementById("shareModal").classList.remove("hidden");
            });

            tr.querySelector("td:first-child").addEventListener("click", () => {
                localStorage.setItem("currentNote", JSON.stringify(note));
                window.location.href = "editor.html";
            });

            tableBody.appendChild(tr);
        });

        document.getElementById("shareBtn").addEventListener("click", shareNote);
        document.getElementById("cancelShareBtn").addEventListener("click", closeShareModal);
        document.querySelector(".new-note").addEventListener("click", () => {
            document.getElementById("newNoteModal").classList.remove("hidden");
        });

        document.getElementById("createNoteSubmitBtn").addEventListener("click", createNote);
        document.getElementById("cancelCreateNoteBtn").addEventListener("click", closeModal);

    } catch (err) {
        console.error("Failed to fetch notes:", err.message);
    }
});

function filterNotes() {
    const searchTerm = searchInput.value.toLowerCase();
    const tagValue = tagFilter.value;

    document.querySelectorAll(".notes-table tbody tr").forEach(row => {
        const title = row.cells[0].textContent.toLowerCase();
        const tag = row.dataset.tag;

        const matchesSearch = title.includes(searchTerm);
        const matchesTag = !tagValue || tag === tagValue;

        row.style.display = matchesSearch && matchesTag ? "" : "none";
    });
}

async function showHistory(noteId, title) {
    const panel = document.getElementById("versionPanel");
    const versionList = panel.querySelector(".version-list");
    const header = panel.querySelector("h3");

    if (!panel.classList.contains("hidden") && header.textContent.includes(title)) {
        panel.classList.add("hidden");
        return;
    }

    try {
        const versions = await getNoteVersions(noteId);
        versionList.innerHTML = "";
        header.textContent = `Version History - ${title}`;

        if (versions.length === 0) {
            versionList.innerHTML = "<li>No versions available.</li>";
        } else {
            versions.forEach(v => {
                const timestamp = new Date(v.createdAt).toLocaleString();
                const preview = v.title || "Untitled";
                const li = document.createElement("li");
                li.innerHTML = `
                    ${timestamp} — <strong>${preview}</strong>
                    <button class="restore-btn" data-version-id="${v.id}">Restore</button>`;
                versionList.appendChild(li);

                li.querySelector('.restore-btn').addEventListener('click', async () => {
                    try {
                        await restoreNoteVersion(noteId, v.id);
                        alert("Version restored successfully!");
                        location.reload();
                    } catch (err) {
                        alert("Restore failed: " + err.message);
                    }
                });
            });

        }

        panel.classList.remove("hidden");
    } catch (err) {
        console.error("Failed to load versions:", err.message);
        versionList.innerHTML = "<li>Error loading versions.</li>";
        panel.classList.remove("hidden");
    }
}

function shareNote() {
    const targetUser = document.getElementById("shareWith").value.trim();
    const permission = document.getElementById("permission").value;

    if (!targetUser) {
        alert("Please enter a username or email.");
        return;
    }

    if (!currentNoteToShare) {
        alert("No note selected for sharing.");
        return;
    }

    shareNoteApi(currentNoteToShare, targetUser, permission)
        .then(() => {
            alert(`Note shared with ${targetUser} as ${permission}`);
            closeShareModal();
        })
        .catch(err => {
            alert("Sharing failed: " + err.message);
        });
}

function closeShareModal() {
    document.getElementById("shareModal").classList.add("hidden");
}

async function createNote() {
    const title = document.getElementById("noteTitle").value.trim();
    const selectedTagInput = document.querySelector("input[name='tag']:checked");
    const tag = selectedTagInput ? selectedTagInput.value : "OTHER";

    if (!title) {
        alert("Please enter a note title.");
        return;
    }

    const user = JSON.parse(localStorage.getItem("user"));
    const defaultContent = "<h1>Start writing...</h1>";

    try {
        const createdNote = await addNote({
            title: title,
            content: defaultContent,
            userId: user.id,
            tag: tag
        });

        localStorage.setItem("currentNote", JSON.stringify(createdNote));

        window.location.href = "editor.html";
    } catch (err) {
        alert("Failed to create note: " + err.message);
    }
}

function closeModal() {
    document.getElementById("newNoteModal").classList.add("hidden");
}