const BASE_URL = "http://localhost:8080";

// Llama al backend y devuelve la respuesta convertida a objeto
async function request<T>(
    endpoint: string,
    method: string = "GET",
    body?: unknown
): Promise<T> {
    const options: RequestInit = {
        method,
        headers: { "Content-Type": "application/json" },
    };

    if (body) {
        options.body = JSON.stringify(body);
    }

    const response = await fetch(`${BASE_URL}${endpoint}`, options);

    // Si el backend devolvio un error, lo lanzamos con su mensaje
    if (!response.ok) {
        const error = await response.json().catch(() => null);
        throw new Error(error?.message || "Error en la peticion");
    }

    // 204 No Content no tiene cuerpo
    if (response.status === 204) {
        return undefined as T;
    }

    return response.json();
}

export const api = {
    get: <T>(endpoint: string) => request<T>(endpoint),
    post: <T>(endpoint: string, body: unknown) => request<T>(endpoint, "POST", body),
    put: <T>(endpoint: string, body: unknown) => request<T>(endpoint, "PUT", body),
    delete: <T>(endpoint: string) => request<T>(endpoint, "DELETE"),
};