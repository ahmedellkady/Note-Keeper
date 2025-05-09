import { updateNote } from "../api/notesApi.js";

let originalTitle = '';
let originalContent = '';

document.addEventListener("DOMContentLoaded", () => {
    const note = JSON.parse(localStorage.getItem("currentNote"));

    if (note && note.permission === "VIEW") {
        disableEditor();
    }

    if (note && note.content) {
        document.getElementById("note-title").textContent = note.title;
        document.getElementById("editor").innerHTML = note.content;

        originalTitle = note.title;
        originalContent = note.content;
    }

    const titleEl = document.getElementById("note-title");

    titleEl.addEventListener("blur", handleTitleSave);
    titleEl.addEventListener("keydown", (e) => {
        if (e.key === "Enter") {
            e.preventDefault();
            titleEl.blur();
        }
    });

    let saveTimeout = null;
    let lastSavedContent = document.getElementById("editor").innerHTML;

    document.getElementById("editor").addEventListener("input", () => {
        clearTimeout(saveTimeout);

        saveTimeout = setTimeout(() => {
            const currentContent = document.getElementById("editor").innerHTML;

            if (currentContent !== lastSavedContent) {
                localStorage.setItem("note-content", currentContent);
                lastSavedContent = currentContent;
                console.log("Auto-saved to localStorage.");
            }
        }, 2000);
    });

    document.querySelector("button[data-format='bold']").addEventListener("click", () => format("bold"));
    document.querySelector("button[data-format='italic']").addEventListener("click", () => format("italic"));
    document.querySelector("button[data-format='underline']").addEventListener("click", () => format("underline"));
    document.querySelector("button[onclick*='highlight']").addEventListener("click", highlight);
    document.querySelector("button[title='Insert Image']").addEventListener("click", insertImage);
    document.querySelector("button[title='Insert List']").addEventListener("click", insertList);
    document.querySelector("button[title='Insert Table']").addEventListener("click", insertTable);
    document.querySelector("select").addEventListener("change", e => applyFormat(e.target.value));
    document.getElementById("imageInput").addEventListener("change", handleImageUpload);
    document.getElementById("saveNoteBtn").addEventListener("click", saveNote);
});

function handleTitleSave() {
    const note = JSON.parse(localStorage.getItem("currentNote"));
    const newTitle = document.getElementById("note-title").textContent.trim();

    if (!newTitle || newTitle === note.title) {
        return; // nothing changed or empty
    }

    const updatedNote = {
        ...note,
        title: newTitle,
    };

    localStorage.setItem("currentNote", JSON.stringify(updatedNote));
    console.log("Title saved to localStorage.");
}

// Update formatting button state
document.addEventListener("selectionchange", updateFormattingState);

function updateFormattingState() {
    const formats = ["bold", "italic", "underline"];
    formats.forEach(format => {
        const isActive = document.queryCommandState(format);
        const btn = document.querySelector(`button[data-format="${format}"]`);
        if (btn) {
            btn.classList.toggle("active", isActive);
        }
    });
}

// Keyboard shortcuts
document.addEventListener("keydown", (e) => {
    if (e.ctrlKey || e.metaKey) {
        let format = null;
        if (e.key.toLowerCase() === "b") format = "bold";
        if (e.key.toLowerCase() === "i") format = "italic";
        if (e.key.toLowerCase() === "u") format = "underline";

        if (format) {
            e.preventDefault();
            document.execCommand(format, false, null);
            updateFormattingState();
        }
    }
});

// Apply block format (h1, h2, p, etc.)
function applyFormat(tag) {
    if (!tag) return;
    const sel = window.getSelection();
    if (!sel.rangeCount) return;

    const range = sel.getRangeAt(0);
    let node = range.startContainer;

    while (node && node !== document.getElementById("editor")) {
        if (/^(P|DIV|H1|H2|H3)$/.test(node.nodeName)) break;
        node = node.parentElement;
    }

    if (!node || node === document.getElementById("editor")) {
        const wrapper = document.createElement(tag);
        wrapper.innerHTML = range.toString() || "<br>";
        range.deleteContents();
        range.insertNode(wrapper);
    } else {
        const replacement = document.createElement(tag);
        replacement.innerHTML = node.innerHTML;
        node.replaceWith(replacement);

        const newRange = document.createRange();
        newRange.selectNodeContents(replacement);
        newRange.collapse(false);
        sel.removeAllRanges();
        sel.addRange(newRange);
    }
}

// Inline formatting
function format(command) {
    document.execCommand(command, false, null);
    updateFormattingState();
}

// Highlight
function highlight() {
    const selection = window.getSelection();
    if (!selection.rangeCount) return;

    const range = selection.getRangeAt(0);
    const ancestor = range.commonAncestorContainer;
    const highlightNode = ancestor.nodeType === 1 ? ancestor : ancestor.parentElement;

    if (highlightNode.classList && highlightNode.classList.contains("highlight")) {
        highlightNode.classList.remove("highlight");
        return;
    }

    const span = document.createElement("span");
    span.className = "highlight";
    range.surroundContents(span);
}

// Image upload
function insertImage() {
    document.getElementById('imageInput').click();
}

function handleImageUpload(event) {
    const file = event.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function (e) {
            const img = document.createElement("img");
            img.src = e.target.result;
            img.style.maxWidth = "100%";
            insertHTMLAtCursor(img.outerHTML);
        };
        reader.readAsDataURL(file);
    }
}

// List
function insertList() {
    const sel = window.getSelection();
    if (!sel.rangeCount) return;

    const range = sel.getRangeAt(0);
    const content = range.extractContents();

    const ul = document.createElement("ul");
    const li = document.createElement("li");
    li.appendChild(content);
    ul.appendChild(li);

    range.deleteContents();
    range.insertNode(ul);
}

// Table
function insertTable() {
    const tableHTML = `
      <table border="1">
        <tr><td>&nbsp;</td><td>&nbsp;</td></tr>
        <tr><td>&nbsp;</td><td>&nbsp;</td></tr>
      </table><br/>`;
    insertHTMLAtCursor(tableHTML);
}

// Insert HTML
function insertHTMLAtCursor(html) {
    const sel = window.getSelection();
    if (sel && sel.rangeCount) {
        const range = sel.getRangeAt(0);
        range.deleteContents();

        const tempDiv = document.createElement("div");
        tempDiv.innerHTML = html;
        const frag = document.createDocumentFragment();
        let node;
        while ((node = tempDiv.firstChild)) {
            frag.appendChild(node);
        }
        range.insertNode(frag);
    }
}

// Table controls
document.addEventListener("click", handleTableClick);

function handleTableClick(e) {
    const existingControls = document.querySelector(".table-controls");
    if (existingControls) existingControls.remove();

    const table = e.target.closest("table");
    if (table) {
        showTableControls(table);
    }
}

function showTableControls(table) {
    const controls = document.createElement("div");
    controls.className = "table-controls";
    controls.innerHTML = `
    <button class="table-btn" data-action="add-row"><i class="fas fa-plus"></i> Row</button>
    <button class="table-btn" data-action="add-col"><i class="fas fa-plus"></i> Col</button>
    <button class="table-btn danger" data-action="del-row"><i class="fas fa-minus"></i> Row</button>
    <button class="table-btn danger" data-action="del-col"><i class="fas fa-minus"></i> Col</button>
  `;

    table.parentElement.insertBefore(controls, table);
    controls.tableRef = table;

    controls.addEventListener("click", (e) => {
        const action = e.target.closest("button")?.dataset.action;
        if (!action) return;

        switch (action) {
            case "add-row":
                addRow(controls);
                break;
            case "add-col":
                addColumn(controls);
                break;
            case "del-row":
                deleteRow(controls);
                break;
            case "del-col":
                deleteColumn(controls);
                break;
        }
    });
}

function addRow(controls) {
    const table = controls.tableRef;
    const row = table.insertRow();
    const colCount = table.rows[0].cells.length;
    for (let i = 0; i < colCount; i++) {
        row.insertCell().innerHTML = "&nbsp;";
    }
}

function addColumn(controls) {
    const table = controls.tableRef;
    for (let row of table.rows) {
        row.insertCell().innerHTML = "&nbsp;";
    }
    const colCount = table.rows[0].cells.length;
    const equalWidth = 100 / colCount + "%";
    for (let row of table.rows) {
        for (let cell of row.cells) {
            cell.style.width = equalWidth;
        }
    }
    table.style.tableLayout = "fixed";
}

function deleteRow(controls) {
    const table = controls.tableRef;
    if (table.rows.length > 1) {
        table.deleteRow(table.rows.length - 1);
    }
}

function deleteColumn(controls) {
    const table = controls.tableRef;
    const colCount = table.rows[0].cells.length;
    if (colCount > 1) {
        for (let row of table.rows) {
            row.deleteCell(colCount - 1);
        }
    }
}

function saveNote() {
    const note = JSON.parse(localStorage.getItem("currentNote"));
    const updatedTitle = document.getElementById("note-title").textContent.trim();
    const updatedContent = document.getElementById("editor").innerHTML;

    updateNote(note.id, {
        title: updatedTitle,
        content: updatedContent,
        tag: note.tag
    })
        .then(updated => {
            localStorage.setItem("currentNote", JSON.stringify(updated));
            console.log("Note saved successfully to backend.");
            alert("Note saved successfully.");
        })
        .catch(err => {
            console.error("Failed to save note:", err.message);
            alert("Error saving note. Please try again.");
        });
}

window.addEventListener("beforeunload", (e) => {
    const currentTitle = document.getElementById("note-title").textContent.trim();
    const currentContent = document.getElementById("editor").innerHTML;

    const hasUnsavedChanges = currentTitle !== originalTitle || currentContent !== originalContent;

    if (hasUnsavedChanges) {
        e.preventDefault();
        e.returnValue = '';
    }
});

function disableEditor() {
    // Make title and editor read-only
    document.getElementById("note-title").setAttribute("contenteditable", "false");
    document.getElementById("editor").setAttribute("contenteditable", "false");

    // Disable all formatting buttons
    document.querySelectorAll("button[data-format], button[onclick], button[title]").forEach(btn => {
        btn.disabled = true;
        btn.classList.add("disabled");
    });

    // Disable dropdowns and inputs
    document.querySelectorAll("select, input[type='color'], input[type='file']").forEach(el => {
        el.disabled = true;
    });

    // Disable save
    document.getElementById("saveNoteBtn").disabled = true;

    // Remove ability to open table controls
    document.removeEventListener("click", handleTableClick);
}