import { registerUser, loginUser } from '../api/authApi.js';

document.addEventListener("DOMContentLoaded", () => {
  const loginForm = document.getElementById("loginForm");
  const signupForm = document.getElementById("signupForm");
  const loginError = document.getElementById("loginError");
  const signupMessage = document.getElementById("signupMessage");

  loginForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const email = document.getElementById("loginEmail").value;
    const password = document.getElementById("loginPassword").value;

    try {
      const user = await loginUser(email, password);
      console.log("Login successful:", user);

      localStorage.setItem("user", JSON.stringify(user));

      window.location.href = "index.html";
    } catch (err) {
      loginError.textContent = err.message;
      loginError.style.display = "block";
    }
  });

  signupForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const name = document.getElementById("name").value;
    const email = document.getElementById("signupEmail").value;
    const password = document.getElementById("signupPassword").value;

    try {
      await registerUser(name, email, password);
      signupMessage.style.display = "block";
    } catch (err) {
      signupMessage.style.display = "block";
      signupMessage.textContent = err.message;
      signupMessage.style.color = "red";
    }
  });
});
