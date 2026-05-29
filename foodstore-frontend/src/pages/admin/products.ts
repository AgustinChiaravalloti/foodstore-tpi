import "../../style.css";
import { api } from "../../utils/api";
import { session } from "../../utils/session";
import type { Producto, Categoria } from "../../types";

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
const precioInput = document.getElementById("precio") as HTMLInputElement;
const stockInput = document.getElementById("stock") as HTMLInputElement;
const categoriaSelect = document.getElementById("categoria") as HTMLSelectElement;
const imagenInput = document.getElementById("imagen") as HTMLInputElement;
const dispInput = document.getElementById("disponible") as HTMLInputElement;

let categorias: Categoria[] = [];

// Cargar categorias para el select
async function cargarCategorias() {
    categorias = await api.get<Categoria[]>("/categoria");
    categoriaSelect.innerHTML = categorias
        .map((c) => `<option value="${c.id}">${c.nombre}</option>`)
        .join("");
}

// Cargar y dibujar la tabla de productos
async function cargar() {
    const productos = await api.get<Producto[]>("/producto");
    tabla.innerHTML = productos
        .map(
            (p) => `
      <tr class="border-t">
        <td class="p-3">${p.id}</td>
        <td class="p-3">${p.nombre}</td>
        <td class="p-3">$${p.precio}</td>
        <td class="p-3">${p.categoria.nombre}</td>
        <td class="p-3">${p.stock}</td>
        <td class="p-3">${p.disponible
                ? '<span class="text-green-600">Disponible</span>'
                : '<span class="text-red-500">No disp.</span>'}</td>
        <td class="p-3">
          <button class="editar text-blue-500 mr-2" data-id="${p.id}">Editar</button>
          <button class="eliminar text-red-500" data-id="${p.id}">Eliminar</button>
        </td>
      </tr>`
        )
        .join("");

    tabla.querySelectorAll(".editar").forEach((b) =>
        b.addEventListener("click", () => {
            const id = Number((b as HTMLElement).dataset.id);
            abrirModal(productos.find((p) => p.id === id));
        })
    );

    tabla.querySelectorAll(".eliminar").forEach((b) =>
        b.addEventListener("click", async () => {
            const id = Number((b as HTMLElement).dataset.id);
            if (confirm("¿Eliminar este producto?")) {
                await api.delete(`/producto/${id}`);
                cargar();
            }
        })
    );
}

function abrirModal(p?: Producto) {
    modalError.classList.add("hidden");
    if (p) {
        modalTitulo.textContent = "Editar Producto";
        editId.value = String(p.id);
        nombreInput.value = p.nombre;
        descInput.value = p.descripcion ?? "";
        precioInput.value = String(p.precio);
        stockInput.value = String(p.stock);
        categoriaSelect.value = String(p.categoria.id);
        imagenInput.value = p.imagen ?? "";
        dispInput.checked = p.disponible;
    } else {
        modalTitulo.textContent = "Nuevo Producto";
        editId.value = "";
        nombreInput.value = "";
        descInput.value = "";
        precioInput.value = "";
        stockInput.value = "";
        imagenInput.value = "";
        dispInput.checked = true;
    }
    modal.classList.remove("hidden");
    modal.classList.add("flex");
}

function cerrarModal() {
    modal.classList.add("hidden");
    modal.classList.remove("flex");
}

document.getElementById("nuevo")!.addEventListener("click", () => abrirModal());
document.getElementById("cancelar")!.addEventListener("click", cerrarModal);

document.getElementById("guardar")!.addEventListener("click", async () => {
    modalError.classList.add("hidden");
    const body = {
        nombre: nombreInput.value,
        descripcion: descInput.value,
        precio: parseFloat(precioInput.value),
        stock: parseInt(stockInput.value),
        imagen: imagenInput.value,
        disponible: dispInput.checked,
        idCategoria: Number(categoriaSelect.value),
    };

    try {
        if (editId.value) {
            await api.put(`/producto/${editId.value}`, body);
        } else {
            await api.post("/producto", body);
        }
        cerrarModal();
        cargar();
    } catch (err) {
        modalError.textContent = err instanceof Error ? err.message : "Error al guardar";
        modalError.classList.remove("hidden");
    }
});

// Inicio
cargarCategorias();
cargar();