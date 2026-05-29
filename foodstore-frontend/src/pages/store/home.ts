import "../../style.css";
import { api } from "../../utils/api";
import { session } from "../../utils/session";
import type { Producto, Categoria } from "../../types";

// Proteccion: si no hay sesion, al login
if (!session.estaLogueado()) {
    window.location.href = "/src/pages/auth/login.html";
}

// Mostrar nombre del usuario
const usuario = session.obtener();
document.getElementById("user-name")!.textContent = usuario?.nombre ?? "";

// Logout
document.getElementById("logout")!.addEventListener("click", () => {
    session.cerrar();
    window.location.href = "/src/pages/auth/login.html";
});

const grid = document.getElementById("productos-grid")!;
const categoriasList = document.getElementById("categorias-list")!;

// Dibuja una lista de productos en la grilla
function renderProductos(productos: Producto[]) {
    if (productos.length === 0) {
        grid.innerHTML = `<p class="text-gray-500">No hay productos.</p>`;
        return;
    }
    grid.innerHTML = productos
        .map(
            (p) => `
      <a href="/src/pages/store/detail.html?id=${p.id}"
         class="bg-white rounded-lg shadow hover:shadow-lg transition p-4 block">
        <img src="${p.imagen || 'https://via.placeholder.com/200'}"
             alt="${p.nombre}" class="w-full h-40 object-cover rounded mb-2" />
        <p class="text-xs text-gray-400">${p.categoria.nombre}</p>
        <h3 class="font-bold">${p.nombre}</h3>
        <p class="text-sm text-gray-500 truncate">${p.descripcion ?? ""}</p>
        <p class="text-lg font-bold text-orange-500 mt-1">$${p.precio}</p>
        ${p.disponible
                ? `<span class="text-xs text-green-600">Disponible</span>`
                : `<span class="text-xs text-red-500">No disponible</span>`}
      </a>`
        )
        .join("");
}

// Carga todos los productos
async function cargarTodos() {
    const productos = await api.get<Producto[]>("/producto");
    renderProductos(productos);
}

// Carga productos de una categoria
async function cargarPorCategoria(idCategoria: number) {
    const productos = await api.get<Producto[]>(`/producto/categoria/${idCategoria}`);
    renderProductos(productos);
}

// Carga el sidebar de categorias
async function cargarCategorias() {
    const categorias = await api.get<Categoria[]>("/categoria");

    // Opcion "Todos"
    const todos = document.createElement("li");
    todos.innerHTML = `<button class="text-gray-700 hover:text-orange-500">Todos los productos</button>`;
    todos.querySelector("button")!.addEventListener("click", cargarTodos);
    categoriasList.appendChild(todos);

    // Una por categoria
    categorias.forEach((c) => {
        const li = document.createElement("li");
        li.innerHTML = `<button class="text-gray-700 hover:text-orange-500">${c.nombre}</button>`;
        li.querySelector("button")!.addEventListener("click", () => cargarPorCategoria(c.id));
        categoriasList.appendChild(li);
    });
}

// Inicio
cargarTodos();
cargarCategorias();