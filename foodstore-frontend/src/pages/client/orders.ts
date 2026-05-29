import "../../style.css";
import { api } from "../../utils/api";
import { session } from "../../utils/session";
import type { Pedido } from "../../types";

// Proteccion de ruta
if (!session.estaLogueado()) {
    window.location.href = "/src/pages/auth/login.html";
}

document.getElementById("logout")!.addEventListener("click", () => {
    session.cerrar();
    window.location.href = "/src/pages/auth/login.html";
});

const lista = document.getElementById("lista")!;

// Color del badge segun estado
function colorEstado(estado: string): string {
    switch (estado) {
        case "PENDIENTE": return "bg-yellow-100 text-yellow-700";
        case "CONFIRMADO": return "bg-blue-100 text-blue-700";
        case "TERMINADO": return "bg-green-100 text-green-700";
        case "CANCELADO": return "bg-red-100 text-red-700";
        default: return "bg-gray-100 text-gray-700";
    }
}

async function cargar() {
    const usuario = session.obtener()!;

    try {
        const pedidos = await api.get<Pedido[]>(`/pedido/usuario/${usuario.id}`);

        if (pedidos.length === 0) {
            lista.innerHTML = `<p class="text-gray-500">Todavia no tenes pedidos.</p>`;
            return;
        }

        lista.innerHTML = pedidos
            .map(
                (p) => `
        <div class="bg-white rounded-lg shadow p-4">
          <div class="flex justify-between items-center mb-2">
            <span class="font-bold">Pedido #${p.id}</span>
            <span class="text-xs px-2 py-1 rounded ${colorEstado(p.estado)}">${p.estado}</span>
          </div>
          <p class="text-sm text-gray-500 mb-2">Fecha: ${p.fecha}</p>
          <ul class="text-sm text-gray-700 mb-2">
            ${p.detalles
                    .map((d) => `<li>${d.cantidad} x ${d.producto.nombre} — $${d.subtotal}</li>`)
                    .join("")}
          </ul>
          <p class="text-right font-bold text-orange-500">Total: $${p.total}</p>
        </div>`
            )
            .join("");
    } catch (err) {
        lista.innerHTML = `<p class="text-red-500">Error al cargar los pedidos.</p>`;
    }
}

cargar();