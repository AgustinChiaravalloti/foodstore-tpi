import "../../style.css";
import { api } from "../../utils/api";
import { session } from "../../utils/session";
import type { Pedido } from "../../types";

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

const lista = document.getElementById("lista")!;
const ESTADOS = ["PENDIENTE", "CONFIRMADO", "TERMINADO", "CANCELADO"];

async function cargar() {
    try {
        const pedidos = await api.get<Pedido[]>("/pedido");

        if (pedidos.length === 0) {
            lista.innerHTML = `<p class="text-gray-500">No hay pedidos.</p>`;
            return;
        }

        lista.innerHTML = pedidos
            .map(
                (p) => `
        <div class="bg-white rounded-lg shadow p-4">
          <div class="flex justify-between items-center mb-2">
            <span class="font-bold">Pedido #${p.id}</span>
            <span class="text-sm text-gray-500">${p.fecha}</span>
          </div>
          <p class="text-sm text-gray-500 mb-1">Cliente ID: ${p.idUsuario} — Pago: ${p.formaPago}</p>
          <ul class="text-sm text-gray-700 mb-2">
            ${p.detalles.map((d) => `<li>${d.cantidad} x ${d.producto.nombre} — $${d.subtotal}</li>`).join("")}
          </ul>
          <p class="font-bold text-orange-500 mb-3">Total: $${p.total}</p>
          <div class="flex items-center gap-2">
            <select class="estado-select border rounded px-2 py-1 text-sm" data-id="${p.id}">
              ${ESTADOS.map(
                    (e) => `<option value="${e}" ${e === p.estado ? "selected" : ""}>${e}</option>`
                ).join("")}
            </select>
            <button class="actualizar bg-green-500 text-white px-3 py-1 rounded text-sm hover:bg-green-600"
                    data-id="${p.id}">
              Actualizar Estado
            </button>
          </div>
        </div>`
            )
            .join("");

        // Conectar botones de actualizar
        lista.querySelectorAll(".actualizar").forEach((b) =>
            b.addEventListener("click", async () => {
                const id = (b as HTMLElement).dataset.id;
                const select = lista.querySelector(`.estado-select[data-id="${id}"]`) as HTMLSelectElement;
                try {
                    await api.put(`/pedido/${id}`, { estado: select.value });
                    alert("Estado actualizado");
                    cargar();
                } catch {
                    alert("Error al actualizar el estado");
                }
            })
        );
    } catch {
        lista.innerHTML = `<p class="text-red-500">Error al cargar los pedidos.</p>`;
    }
}

cargar();