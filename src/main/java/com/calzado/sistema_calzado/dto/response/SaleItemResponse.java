package com.calzado.sistema_calzado.dto.response;

import com.calzado.sistema_calzado.model.SaleItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleItemResponse {

    private Long id;
    private Long productId;
    private String productName;
    private String productCode;
    private String size;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;

    public static SaleItemResponse fromSaleItem(SaleItem item) {
        return SaleItemResponse.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProductName())
                .productCode(item.getProductCode())
                .size(item.getSize())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .subtotal(item.getSubtotal())
                .build();
    }
}