package com.calzado.sistema_calzado.dto.response;

import com.calzado.sistema_calzado.model.ProductStock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockResponse {

    private Long id;
    private String size;
    private Integer quantity;
    private Integer reservedQuantity;
    private Integer availableQuantity;

    public static ProductStockResponse fromProductStock(ProductStock stock) {
        return ProductStockResponse.builder()
                .id(stock.getId())
                .size(stock.getSize())
                .quantity(stock.getQuantity())
                .reservedQuantity(stock.getReservedQuantity())
                .availableQuantity(stock.getAvailableQuantity())
                .build();
    }
}
