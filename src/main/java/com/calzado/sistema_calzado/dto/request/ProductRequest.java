package com.calzado.sistema_calzado.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductRequest {

    @NotBlank(message = "Código es requerido")
    private String code;

    @NotBlank(message = "Nombre es requerido")
    private String name;

    private String description;

    @NotBlank(message = "Marca es requerida")
    private String brand;

    @NotBlank(message = "Categoría es requerida")
    private String category;

    @NotBlank(message = "Color es requerido")
    private String color;

    @NotBlank(message = "Material es requerido")
    private String material;

    @NotNull(message = "Precio de compra es requerido")
    @DecimalMin(value = "0.0", message = "El precio debe ser mayor a 0")
    private BigDecimal purchasePrice;

    @NotNull(message = "Precio de venta es requerido")
    @DecimalMin(value = "0.0", message = "El precio debe ser mayor a 0")
    private BigDecimal salePrice;

    private Integer minStock = 5;

    private Boolean isActive = true;

    private List<ProductStockRequest> stocks;
}