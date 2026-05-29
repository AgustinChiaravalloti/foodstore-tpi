import "../../style.css";
import { api } from "../../utils/api";
import { session } from "../../utils/session";
import type { Categoria } from "../../types";

// Proteccion admin
if (!session.estaLogueado()) {
    window.location.href = "/src/pages/auth/login.html";
} else if (!session.esAdmin()) {
    window.location.href = "/src/pages/store/home.html";
}

document.getElementById("logout")!.addEventListener("click", () => {
    session.cerrar();
    window.location.href = "/src/pages/auth/login.html";
});

const tabla = document.getElementById("tabla")!;
const modal = document.getElementById("modal")!;
const modalTitulo = document.getElementById("modal-titulo")!;
const modalError = document.getElementById("modal-error")!;
const editId = document.getElementById("edit-id") as HTMLInputElement;
const nombreInput = document.getElementById("nombre") as HTMLInputElement;
const descInput = document.getElementById("descripcion") as HTMLTextAreaElement;

// Cargar y dibujar la tabla
async function cargar() {
    const categorias = await api.get<Categoria[]>("/categoria");
    tabla.innerHTML = categorias
        .map(
            (c) => `
      <tr class="border-t">
        <td class="p-3">${c.id}</td>
        <td class="p-3">${c.nombre}</td>
        <td class="p-3">${c.descripcion ?? ""}</td>
        <td class="p-3">
          <button class="editar text-blue-500 mr-2" data-id="${c.id}">Editar</button>
          <button class="eliminar text-red-500" data-id="${c.id}">Eliminar</button>
        </td>
      </tr>`
        )
        .join("");

    // Conectar botones editar
    tabla.querySelectorAll(".editar").forEach((b) =>
        b.addEventListener("click", () => {
            const id = Number((b as HTMLElement).dataset.id);
            const cat = categorias.find((c) => c.id === id)!;
            abrirModal(cat);
        })
    );

    // Conectar botones eliminar
    tabla.querySelectorAll(".eliminar").forEach((b) =>
        b.addEventListener("click", async () => {
            const id = Number((b as HTMLElement).dataset.id);
            if (confirm("¿Eliminar esta categoria?")) {
                await api.delete(`/categoria/${id}`);
                cargar();
            }
        })
    );
}

// Abrir modal (sin argumento = crear, con categoria = editar)
function abrirModal(cat?: Categoria) {
    modalError.classList.add("hidden");
    if (cat) {
        modalTitulo.textContent = "Editar Categoria";
        editId.value = String(cat.id);
        nombreInput.value = cat.nombre;
        descInput.value = cat.descripcion ?? "";
    } else {
        modalTitulo.textContent = "Nueva Categoria";
        editId.value = "";
        nombreInput.value = "";
        descInput.value = "";
    }
    modal.classList.remove("hidden");
    modal.classList.add("flex");
}

function cerrarModal() {
    modal.classList.add("hidden");
    modal.classList.remove("flex");
}

document.getElementById("nueva")!.addEventListener("click", () => abrirModal());
document.getElementById("cancelar")!.addEventListener("click", cerrarModal);

// Guardar (crear o editar segun haya id)
document.getElementById("guardar")!.addEventListener("click", async () => {
    modalError.classList.add("hidden");
    const body = { nombre: nombreInput.value, descripcion: descInput.value };

    try {
        if (editId.value) {
            await api.put(`/categoria/${editId.value}`, body); // editar
        } else {
            await api.post("/categoria", body); // crear
        }
        cerrarModal();
        cargar();
    } catch (err) {
        modalError.textContent = err instanceof Error ? err.message : "Error al guardar";
        modalError.classList.remove("hidden");
    }
});

cargar();