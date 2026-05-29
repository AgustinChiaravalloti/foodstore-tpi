package com.utn.foodstore.service;

import com.utn.foodstore.dto.*;
import com.utn.foodstore.exception.BusinessException;
import com.utn.foodstore.model.*;
import com.utn.foodstore.repository.PedidoRepository;
import com.utn.foodstore.repository.ProductoRepository;
import com.utn.foodstore.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    public PedidoService(PedidoRepository repository,
                         UsuarioRepository usuarioRepository,
                         ProductoRepository productoRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
    }

    // HU-017: Crear pedido (TRANSACCIONAL: todo o nada)
    @Transactional
    public PedidoDto save(PedidoCreate dto) {
        // 1. Validar que el usuario exista (RN-017-01)
        Usuario usuario = usuarioRepository.findByIdOrThrow(dto.idUsuario());

        // 2. Crear el pedido base (todavia sin total)
        Pedido pedido = Pedido.builder()
                .fecha(LocalDate.now())          // fecha actual (RN-017-11)
                .estado(dto.estado())
                .formaPago(dto.formaPago())
                .usuario(usuario)
                .total(BigDecimal.ZERO)          // se calcula al final
                .detalles(new ArrayList<>())
                .build();

        // 3. Procesar cada detalle: validar producto, stock y crear la linea
        for (DetallePedidoCreate detalleDto : dto.detallePedido()) {
            Producto producto = productoRepository.findByIdOrThrow(detalleDto.idProducto());

            // Producto disponible (RN-017-06)
            if (!producto.isDisponible()) {
                throw new BusinessException(
                        "El producto '" + producto.getNombre() + "' no está disponible para la venta");
            }

            // Stock suficiente (RN-017-07)
            if (!producto.tieneStockSuficiente(detalleDto.cantidad())) {
                throw new BusinessException(
                        "Stock insuficiente para '" + producto.getNombre()
                                + "'. Disponible: " + producto.getStock()
                                + ", Solicitado: " + detalleDto.cantidad());
            }

            // Subtotal = precio * cantidad (RN-017-09)
            BigDecimal subtotal = producto.getPrecio()
                    .multiply(BigDecimal.valueOf(detalleDto.cantidad()));

            // Crear la linea de detalle y vincularla al pedido
            DetallePedido detalle = DetallePedido.builder()
                    .producto(producto)
                    .cantidad(detalleDto.cantidad())
                    .subtotal(subtotal)
                    .pedido(pedido)
                    .build();

            pedido.getDetalles().add(detalle);

            // Reducir el stock del producto (RN-017-12)
            producto.reducirStock(detalleDto.cantidad());
            productoRepository.save(producto);
        }

        // 4. Calcular el total (suma de subtotales) - usa Calculable (RN-017-10)
        pedido.calcularTotal();

        // 5. Guardar pedido + detalles (cascade los guarda juntos)
        Pedido guardado = repository.save(pedido);
        return toDto(guardado);
    }

    // HU-018: Listar todos los pedidos
    public List<PedidoDto> findAll() {
        return repository.findAllActive()
                .stream()
                .map(this::toDto)
                .toList();
    }

    // HU-019: Obtener pedido por id
    public PedidoDto findById(Long id) {
        return toDto(repository.findByIdOrThrow(id));
    }

    // HU-020: Listar pedidos de un usuario
    public List<PedidoDto> findByUsuario(Long idUsuario) {
        usuarioRepository.findByIdOrThrow(idUsuario); // 404 si no existe (RN-020-01)
        return repository.findAllByUsuarioIdAndEliminadoFalse(idUsuario)
                .stream()
                .map(this::toDto)
                .toList();
    }

    // HU-021: Actualizar estado y/o forma de pago (parcial)
    public PedidoDto update(Long id, PedidoEdit dto) {
        Pedido pedido = repository.findByIdOrThrow(id);

        if (dto.estado() != null)    pedido.setEstado(dto.estado());
        if (dto.formaPago() != null) pedido.setFormaPago(dto.formaPago());

        return toDto(repository.save(pedido));
    }

    // HU-022: Eliminar pedido (soft delete)
    public void delete(Long id) {
        repository.softDeleteById(id);
    }

    // --- Traductores entidad -> DTO ---

    private PedidoDto toDto(Pedido p) {
        List<DetallePedidoDto> detalles = p.getDetalles()
                .stream()
                .map(this::toDetalleDto)
                .toList();

        return new PedidoDto(
                p.getId(),
                p.getFecha(),
                p.getEstado(),
                p.getTotal(),
                p.getFormaPago(),
                p.getUsuario().getId(),
                detalles
        );
    }

    private DetallePedidoDto toDetalleDto(DetallePedido d) {
        Producto prod = d.getProducto();
        Categoria cat = prod.getCategoria();
        CategoriaDto catDto = new CategoriaDto(cat.getId(), cat.getNombre(), cat.getDescripcion());
        ProductoDto prodDto = new ProductoDto(
                prod.getId(), prod.getNombre(), prod.getPrecio(), prod.getDescripcion(),
                prod.getStock(), prod.getImagen(), prod.isDisponible(), catDto);

        return new DetallePedidoDto(d.getId(), d.getCantidad(), d.getSubtotal(), prodDto);
    }
}