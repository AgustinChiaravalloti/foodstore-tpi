import "../../style.css";
import { api } from "../../utils/api";
import { session } from "../../utils/session";
import type { Usuario } from "../../types";

const form = document.getElementById("login-form") as HTMLFormElement;
const mailInput = document.getElementById("mail") as HTMLInputElement;
const passwordInput = document.getElementById("password") as HTMLInputElement;
const errorMsg = document.getElementById("error") as HTMLParagraphElement;

form.addEventListener("submit", async (e) => {
    e.preventDefault(); // evita que la pagina se recargue al enviar
    errorMsg.classList.add("hidden");

    try {
        // Llama al backend: POST /auth/login
        const usuario = await api.post<Usuario>("/auth/login", {
            mail: mailInput.value,
            password: passwordInput.value,
        });

        // Guarda la sesion en localStorage
        session.guardar(usuario);

        // Redirige segun el rol
        if (usuario.rol === "ADMIN") {
            window.location.href = "/src/pages/admin/dashboard.html";
        } else {
            window.location.href = "/src/pages/store/home.html";
        }
    } catch (err) {
        // Muestra el mensaje de error del backend
        errorMsg.textContent = err instanceof Error ? err.message : "Error al iniciar sesion";
        errorMsg.classList.remove("hidden");
    }
});