# Food Store - Sistema de Gestión de Pedidos de Comida

Trabajo Práctico Integrador - Programación 3
Tecnicatura Universitaria en Programación a Distancia (UTN)

Aplicación web full stack para la gestión de un negocio de comidas. Permite a los administradores gestionar categorías, productos y pedidos, y a los clientes navegar el catálogo, usar un carrito y realizar compras.

## Tecnologías

**Backend:** Spring Boot 3.x, Java 21, Spring Data JPA, H2 Database, Gradle
**Frontend:** TypeScript, Vite, Tailwind CSS

## Estructura del proyecto
foodstoreTPI/
├── foodstore-backend/    # API REST (Spring Boot)
└── foodstore-frontend/   # Interfaz de usuario (Vite + TypeScript)

## Requisitos previos

- Java 21 o superior
- Node.js 18 o superior
- Git

## Cómo ejecutar el Backend

1. Posicionarse en la carpeta del backend:
cd foodstore-backend

2. Ejecutar la aplicación:
./gradlew bootRun

(En Windows: `gradlew.bat bootRun`)
3. El backend queda corriendo en `http://localhost:8080`.

### Base de datos

El proyecto usa **H2** (base de datos en memoria), por lo que no requiere instalación adicional. La base se crea automáticamente al arrancar y se reinicia en cada ejecución.

- Consola H2: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:foodstore`
- Usuario: `sa` (sin contraseña)

Al iniciar, se crea automáticamente un usuario administrador:
- Email: `admin@admin.com`
- Contraseña: `123456`

### Documentación de la API

Con el backend corriendo: `http://localhost:8080/swagger-ui.html`

## Cómo ejecutar el Frontend

1. Posicionarse en la carpeta del frontend:
cd foodstore-frontend
2. Instalar dependencias (solo la primera vez):
npm install
3. Ejecutar:
npm run dev
4. Abrir en el navegador la página de login:
   `http://localhost:5173/src/pages/auth/login.html`

> Importante: el backend debe estar corriendo para que el frontend funcione.

## Funcionalidades

**Cliente:** registro, login, navegación del catálogo, filtro por categoría, detalle de producto, carrito persistente, confirmación de compra e historial de pedidos.

**Administrador:** dashboard con estadísticas, CRUD de categorías, CRUD de productos y gestión del estado de los pedidos.

## Autor

Agustín Chiaravalloti