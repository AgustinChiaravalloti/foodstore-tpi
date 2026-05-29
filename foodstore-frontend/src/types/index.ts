// Refleja los DTOs del backend

export type Rol = "ADMIN" | "USUARIO";
export type Estado = "PENDIENTE" | "CONFIRMADO" | "TERMINADO" | "CANCELADO";
export type FormaPago = "TARJETA" | "TRANSFERENCIA" | "EFECTIVO";

export interface Usuario {
    id: number;
    nombre: string;
    apellido: string;
    mail: string;
    celular: string | null;
    rol: Rol;
}

export interface Categoria {
    id: number;
    nombre: string;
    descripcion: string | null;
}

export interface Producto {
    id: number;
    nombre: string;
    precio: number;
    descripcion: string | null;
    stock: number;
    imagen: string | null;
    disponible: boolean;
    categoria: Categoria;
}

export interface DetallePedido {
    id: number;
    cantidad: number;
    subtotal: number;
    producto: Producto;
}

export interface Pedido {
    id: number;
    fecha: string;
    estado: Estado;
    total: number;
    formaPago: FormaPago;
    idUsuario: number;
    detalles: DetallePedido[];
}