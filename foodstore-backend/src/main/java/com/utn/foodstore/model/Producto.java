package com.utn.foodstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "producto")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Producto extends Base {

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private BigDecimal precio;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false)
    private Integer stock;

    private String imagen;

    @Column(nullable = false)
    @lombok.Builder.Default
    private boolean disponible = true;

    // Relacion: muchos productos -> una categoria
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    // ¿Hay stock suficiente para la cantidad pedida?
    public boolean tieneStockSuficiente(int cantidad) {
        return this.stock >= cantidad;
    }

    // Reduce el stock en la cantidad indicada
    public void reducirStock(int cantidad) {
        this.stock = this.stock - cantidad;
    }
}