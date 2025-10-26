package com.calzado.sistema_calzado.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductStockRequest {

    @NotBlank(message = "Talla es requerida")
    private String size;

    @NotNull(message = "Cantidad es requerida")
    @Min(value = 0, message = "La cantidad debe ser mayor o igual a 0")
    private Integer quantity;
}