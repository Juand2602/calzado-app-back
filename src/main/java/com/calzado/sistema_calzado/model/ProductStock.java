package com.calzado.sistema_calzado.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_stock", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"product_id", "size"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonBackReference
    private Product product;

    @Column(nullable = false, length = 10)
    @NotBlank(message = "Talla es requerida")
    private String size;

    @Column(nullable = false)
    @NotNull(message = "Cantidad es requerida")
    @Min(value = 0, message = "La cantidad debe ser mayor o igual a 0")
    private Integer quantity = 0;

    @Column(name = "reserved_quantity", nullable = false)
    @Min(value = 0, message = "La cantidad reservada debe ser mayor o igual a 0")
    private Integer reservedQuantity = 0;

    public Integer getAvailableQuantity() {
        return quantity - reservedQuantity;
    }

    public void reserveStock(Integer amount) {
        if (getAvailableQuantity() < amount) {
            throw new IllegalArgumentException("Stock insuficiente para reservar");
        }
        this.reservedQuantity += amount;
    }

    public void releaseReservedStock(Integer amount) {
        this.reservedQuantity = Math.max(0, this.reservedQuantity - amount);
    }

    public void confirmSale(Integer amount) {
        if (this.reservedQuantity < amount) {
            throw new IllegalArgumentException("No hay suficiente stock reservado");
        }
        this.quantity -= amount;
        this.reservedQuantity -= amount;
    }
}