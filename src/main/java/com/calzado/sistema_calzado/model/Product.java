package com.calzado.sistema_calzado.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    @NotBlank(message = "Código es requerido")
    private String code;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Nombre es requerido")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "Marca es requerida")
    private String brand;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "Categoría es requerida")
    private String category;

    @Column(nullable = false, length = 30)
    @NotBlank(message = "Color es requerido")
    private String color;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "Material es requerido")
    private String material;

    @Column(name = "purchase_price", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Precio de compra es requerido")
    @DecimalMin(value = "0.0", message = "El precio debe ser mayor o igual a 0")
    private BigDecimal purchasePrice;

    @Column(name = "sale_price", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Precio de venta es requerido")
    @DecimalMin(value = "0.0", message = "El precio debe ser mayor o igual a 0")
    private BigDecimal salePrice;

    @Column(name = "min_stock", nullable = false)
    private Integer minStock = 5;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ProductStock> stocks = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (isActive == null) isActive = true;
        if (minStock == null) minStock = 5;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Métodos auxiliares
    public Integer getTotalStock() {
        return stocks.stream()
                .mapToInt(ProductStock::getQuantity)
                .sum();
    }

    public Boolean isLowStock() {
        return getTotalStock() <= minStock;
    }

    public void addStock(ProductStock stock) {
        stocks.add(stock);
        stock.setProduct(this);
    }
}
