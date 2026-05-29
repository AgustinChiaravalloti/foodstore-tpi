import "../../style.css";
import { api } from "../../utils/api";
import { session } from "../../utils/session";
import { cart } from "../../utils/cart";
import type { Producto } from "../../types";

// Proteccion de ruta
if (!session.estaLogueado()) {
    window.location.href = "/src/pages/auth/login.html";
}

// Logout
document.getElementById("logout")!.addEventListener("click", () => {
    session.cerrar();
    window.location.href = "/src/pages/auth/login.html";
});

// Badge del carrito
const cartCount = document.getElementById("cart-count")!;
cartCount.textContent = String(cart.cantidadTotal());

const contenedor = document.getElementById("detalle")!;

// Lee el id del producto desde la URL (?id=...)
const params = new URLSearchParams(window.location.search);
const id = params.get("id");

async function cargar() {
    if (!id) {
        contenedor.innerHTML = `<p class="text-red-500">Producto no especificado.</p>`;
        return;
    }

    try {
        const p = await api.get<Producto>(`/producto/${id}`);

        const sinStock = p.stock <= 0 || !p.disponible;

        contenedor.innerHTML = `
      <img src="${p.imagen || 'https://via.placeholder.com/300'}"
           alt="${p.nombre}" class="w-full md:w-80 h-72 object-cover rounded" />
      <div class="flex-1">
        <p class="text-sm text-gray-400">${p.categoria.nombre}</p>
        <h1 class="text-2xl font-bold mb-2">${p.nombre}</h1>
        <p class="text-3xl font-bold text-orange-500 mb-2">$${p.precio}</p>
        <p class="text-gray-600 mb-2">${p.descripcion ?? ""}</p>
        <p class="text-sm mb-1">Stock disponible: <b>${p.stock}</b></p>
        <p class="text-sm mb-4">
          ${p.disponible
            ? '<span class="text-green-600">Disponible</span>'
            : '<span class="text-red-500">No disponible</span>'}
        </p>

        <div class="flex items-center gap-2 mb-4">
          <label class="text-sm">Cantidad:</label>
          <input id="cantidad" type="number" value="1" min="1" max="${p.stock}"
                 class="border rounded w-20 px-2 py-1" ${sinStock ? "disabled" : ""} />
        </div>

        <button id="agregar"
          class="bg-orange-500 text-white px-6 py-2 rounded font-medium hover:bg-orange-600 disabled:bg-gray-300"
          ${sinStock ? "disabled" : ""}>
          Agregar al Carrito
        </button>
        <p id="msg" class="text-green-600 text-sm mt-2 hidden">Agregado al carrito</p>
      </div>
    `;

        if (!sinStock) {
            document.getElementById("agregar")!.addEventListener("click", () => {
                const cantidadInput = document.getElementById("cantidad") as HTMLInputElement;
                const cantidad = parseInt(cantidadInput.value);

                // Validaciones de stock (lo pide el TP)
                if (cantidad < 1) return;
                if (cantidad > p.stock) {
                    alert("La cantidad supera el stock disponible");
                    return;
                }

                cart.agregar(p, cantidad);
                cartCount.textContent = String(cart.cantidadTotal());

                const msg = document.getElementById("msg")!;
                msg.classList.remove("hidden");
                setTimeout(() => msg.classList.add("hidden"), 2000);
            });
        }
    } catch {
        contenedor.innerHTML = `<p class="text-red-500">Producto no encontrado.</p>`;
    }
}

cargar();