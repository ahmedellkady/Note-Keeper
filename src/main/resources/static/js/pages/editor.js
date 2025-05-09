// Save note content every 2 seconds
setInterval(() => {
    const content = document.getElementById("editor").innerHTML;
    localStorage.setItem("note-content", content);
}, 2000);

document.addEventListener("DOMContentLoaded", () => {
    const saved = localStorage.getItem("note-content");
    if (saved) {
        document.getElementById("editor").innerHTML = saved;
    }
});

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
document.addEventListener("click", function (e) {
    const existingControls = document.querySelector(".table-controls");
    if (existingControls) existingControls.remove();

    const table = e.target.closest("table");
    if (table) {
        showTableControls(table);
    }
});

function showTableControls(table) {
    const controls = document.createElement("div");
    controls.className = "table-controls";
    controls.innerHTML = `
      <button class="table-btn" onclick="addRow(this)">
        <i class="fas fa-plus"></i> Row
      </button>
      <button class="table-btn" onclick="addColumn(this)">
        <i class="fas fa-plus"></i> Col
      </button>
      <button class="table-btn danger" onclick="deleteRow(this)">
        <i class="fas fa-minus"></i> Row
      </button>
      <button class="table-btn danger" onclick="deleteColumn(this)">
        <i class="fas fa-minus"></i> Col
      </button>`;

    table.parentElement.insertBefore(controls, table);
    controls.tableRef = table;
}

function addRow(button) {
    const table = button.parentElement.tableRef;
    const row = table.insertRow();
    const colCount = table.rows[0].cells.length;
    for (let i = 0; i < colCount; i++) {
        row.insertCell().innerHTML = "&nbsp;";
    }
}

function addColumn(button) {
    const table = button.parentElement.tableRef;
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

function deleteRow(button) {
    const table = button.parentElement.tableRef;
    if (table.rows.length > 1) {
        table.deleteRow(table.rows.length - 1);
    }
}

function deleteColumn(button) {
    const table = button.parentElement.tableRef;
    const colCount = table.rows[0].cells.length;
    if (colCount > 1) {
        for (let row of table.rows) {
            row.deleteCell(colCount - 1);
        }
    }
}