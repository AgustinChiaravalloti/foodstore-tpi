import "../../style.css";
import { api } from "../../utils/api";
import { session } from "../../utils/session";
import { cart } from "../../utils/cart";
import type { Pedido } from "../../types";

// Proteccion de ruta
if (!session.estaLogueado()) {
    window.location.href = "/src/pages/auth/login.html";
}

document.getElementById("logout")!.addEventListener("click", () => {
    session.cerrar();
    window.location.href = "/src/pages/auth/login.html";
});

const itemsDiv = document.getElementById("items")!;
const resumen = document.getElementById("resumen")!;
const vacio = document.getElementById("vacio")!;
const totalSpan = document.getElementById("total")!;
const modal = document.getElementById("modal")!;

// Dibuja el carrito
function render() {
    const items = cart.obtener();

    if (items.length === 0) {
        itemsDiv.innerHTML = "";
        resumen.classList.add("hidden");
        vacio.classList.remove("hidden");
        return;
    }

    vacio.classList.add("hidden");
    resumen.classList.remove("hidden");

    itemsDiv.innerHTML = items
        .map(
            (i) => `
      <div class="bg-white rounded-lg shadow p-4 flex items-center gap-4">
        <img src="${i.producto.imagen || 'https://via.placeholder.com/80'}"
             class="w-20 h-20 object-cover rounded" />
        <div class="flex-1">
          <h3 class="font-bold">${i.producto.nombre}</h3>
          <p class="text-sm text-gray-500">$${i.producto.precio} c/u</p>
        </div>
        <div class="flex items-center gap-2">
          <button class="menos px-2 bg-gray-200 rounded" data-id="${i.producto.id}">-</button>
          <span>${i.cantidad}</span>
          <button class="mas px-2 bg-gray-200 rounded" data-id="${i.producto.id}">+</button>
        </div>
        <p class="font-bold w-24 text-right">$${i.producto.precio * i.cantidad}</p>
        <button class="eliminar text-red-500" data-id="${i.producto.id}">X</button>
      </div>`
        )
        .join("");

    totalSpan.textContent = `$${cart.total()}`;

    // Conectar botones + / - / eliminar
    itemsDiv.querySelectorAll(".mas").forEach((b) =>
        b.addEventListener("click", () => {
            const id = Number((b as HTMLElement).dataset.id);
            const item = cart.obtener().find((i) => i.producto.id === id)!;
            if (item.cantidad < item.producto.stock) {
                cart.cambiarCantidad(id, item.cantidad + 1);
                render();
            } else {
                alert("No hay mas stock disponible");
            }
        })
    );

    itemsDiv.querySelectorAll(".menos").forEach((b) =>
        b.addEventListener("click", () => {
            const id = Number((b as HTMLElement).dataset.id);
            const item = cart.obtener().find((i) => i.producto.id === id)!;
            if (item.cantidad > 1) {
                cart.cambiarCantidad(id, item.cantidad - 1);
                render();
            }
        })
    );

    itemsDiv.querySelectorAll(".eliminar").forEach((b) =>
        b.addEventListener("click", () => {
            cart.quitar(Number((b as HTMLElement).dataset.id));
            render();
        })
    );
}

// Botones del resumen
document.getElementById("vaciar")!.addEventListener("click", () => {
    cart.vaciar();
    render();
});

document.getElementById("checkout")!.addEventListener("click", () => {
    modal.classList.remove("hidden");
    modal.classList.add("flex");
});

document.getElementById("cancelar")!.addEventListener("click", () => {
    modal.classList.add("hidden");
    modal.classList.remove("flex");
});

// Confirmar pedido -> POST /pedido
document.getElementById("confirmar")!.addEventListener("click", async () => {
    const telefono = (document.getElementById("telefono") as HTMLInputElement).value;
    const formaPago = (document.getElementById("formaPago") as HTMLSelectElement).value;
    const errorMsg = document.getElementById("modal-error")!;

    errorMsg.classList.add("hidden");

    if (!telefono.trim()) {
        errorMsg.textContent = "El telefono es requerido";
        errorMsg.classList.remove("hidden");
        return;
    }

    const usuario = session.obtener()!;
    const items = cart.obtener();

    try {
        await api.post<Pedido>("/pedido", {
            estado: "PENDIENTE",
            formaPago: formaPago,
            idUsuario: usuario.id,
            detallePedido: items.map((i) => ({
                idProducto: i.producto.id,
                cantidad: i.cantidad,
            })),
        });

        cart.vaciar();
        alert("Pedido confirmado con exito!");
        window.location.href = "/src/pages/client/orders.html";
    } catch (err) {
        errorMsg.textContent = err instanceof Error ? err.message : "Error al crear el pedido";
        errorMsg.classList.remove("hidden");
    }
});

render();