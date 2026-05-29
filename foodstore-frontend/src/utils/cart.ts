import type { Producto } from "../types";

const KEY = "carrito";

export interface ItemCarrito {
    producto: Producto;
    cantidad: number;
}

export const cart = {
    // Devuelve los items del carrito
    obtener(): ItemCarrito[] {
        const data = localStorage.getItem(KEY);
        return data ? JSON.parse(data) : [];
    },

    // Guarda la lista de items
    guardar(items: ItemCarrito[]): void {
        localStorage.setItem(KEY, JSON.stringify(items));
    },

    // Agrega un producto (suma cantidad si ya estaba)
    agregar(producto: Producto, cantidad: number): void {
        const items = this.obtener();
        const existente = items.find((i) => i.producto.id === producto.id);
        if (existente) {
            existente.cantidad += cantidad;
        } else {
            items.push({ producto, cantidad });
        }
        this.guardar(items);
    },

    // Quita un producto del carrito
    quitar(idProducto: number): void {
        this.guardar(this.obtener().filter((i) => i.producto.id !== idProducto));
    },

    // Cambia la cantidad de un item
    cambiarCantidad(idProducto: number, cantidad: number): void {
        const items = this.obtener();
        const item = items.find((i) => i.producto.id === idProducto);
        if (item) item.cantidad = cantidad;
        this.guardar(items);
    },

    // Vacia el carrito
    vaciar(): void {
        localStorage.removeItem(KEY);
    },

    // Cantidad total de items (para el badge)
    cantidadTotal(): number {
        return this.obtener().reduce((acc, i) => acc + i.cantidad, 0);
    },

    // Precio total
    total(): number {
        return this.obtener().reduce((acc, i) => acc + i.producto.precio * i.cantidad, 0);
    },
};