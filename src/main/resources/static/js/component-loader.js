document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll("[data-include]").forEach(async el => {
        const file = el.getAttribute("data-include");
        try {
            const response = await fetch(file);
            const html = await response.text();
            el.innerHTML = html;
        } catch (e) {
            console.error("Error loading component:", file, e);
        }
    });
});
