import "../../style.css";
import { api } from "../../utils/api";
import { session } from "../../utils/session";
import type { Usuario } from "../../types";

const form = document.getElementById("register-form") as HTMLFormElement;
const errorMsg = document.getElementById("error") as HTMLParagraphElement;

const getValue = (id: string) =>
    (document.getElementById(id) as HTMLInputElement).value;

form.addEventListener("submit", async (e) => {
    e.preventDefault();
    errorMsg.classList.add("hidden");

    try {
        // Registra el usuario: POST /usuario
        await api.post<Usuario>("/usuario", {
            nombre: getValue("nombre"),
            apellido: getValue("apellido"),
            mail: getValue("mail"),
            celular: getValue("celular") || null,
            password: getValue("password"),
        });

        // Auto-login despues del registro (lo pide el TP)
        const usuario = await api.post<Usuario>("/auth/login", {
            mail: getValue("mail"),
            password: getValue("password"),
        });

        session.guardar(usuario);
        window.location.href = "/src/pages/store/home.html";
    } catch (err) {
        errorMsg.textContent = err instanceof Error ? err.message : "Error al registrarse";
        errorMsg.classList.remove("hidden");
    }
});