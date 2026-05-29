import "../../style.css";
import { api } from "../../utils/api";
import { session } from "../../utils/session";
import type { Categoria, Producto, Pedido } from "../../types";

// Proteccion: debe haber sesion Y ser ADMIN
if (!session.estaLogueado()) {
    window.location.href = "/src/pages/auth/login.html";
} else if (!session.esAdmin()) {
    // Un usuario normal no puede entrar al panel admin
    window.location.href = "/src/pages/store/home.html";
}

document.getElementById("logout")!.addEventListener("click", () => {
    session.cerrar();
    window.location.href = "/src/pages/auth/login.html";
});

async function cargarStats() {
    try {
        const [categorias, productos, pedidos] = await Promise.all([
            api.get<Categoria[]>("/categoria"),
            api.get<Producto[]>("/producto"),
            api.get<Pedido[]>("/pedido"),
        ]);

        document.getElementById("stat-categorias")!.textContent = String(categorias.length);
        document.getElementById("stat-productos")!.textContent = String(productos.length);
        document.getElementById("stat-pedidos")!.textContent = String(pedidos.length);

        const disponibles = productos.filter((p) => p.disponible).length;
        document.getElementById("stat-disponibles")!.textContent = String(disponibles);
    } catch {
        console.error("Error al cargar estadisticas");
    }
}

cargarStats();