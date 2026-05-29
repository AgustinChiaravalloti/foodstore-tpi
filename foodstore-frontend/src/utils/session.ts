import type { Usuario } from "../types";

const KEY = "usuario";

export const session = {
    // Guarda el usuario logueado en localStorage
    guardar(usuario: Usuario): void {
        localStorage.setItem(KEY, JSON.stringify(usuario));
    },

    // Devuelve el usuario logueado, o null si no hay sesion
    obtener(): Usuario | null {
        const data = localStorage.getItem(KEY);
        return data ? JSON.parse(data) : null;
    },

    // Cierra sesion
    cerrar(): void {
        localStorage.removeItem(KEY);
    },

    // ¿Hay alguien logueado?
    estaLogueado(): boolean {
        return this.obtener() !== null;
    },

    // ¿El usuario logueado es ADMIN?
    esAdmin(): boolean {
        return this.obtener()?.rol === "ADMIN";
    },
};