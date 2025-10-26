package com.calzado.sistema_calzado.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SaleItemRequest {

    @NotNull(message = "ID del producto es requerido")
    private Long productId;

    @NotNull(message = "Talla es requerida")
    private String size;

    @NotNull(message = "Cantidad es requerida")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer quantity;
}
