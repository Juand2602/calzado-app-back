package com.calzado.sistema_calzado.dto.response;

import com.calzado.sistema_calzado.model.Product;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Long id;
    private String code;
    private String name;
    private String description;
    private String brand;
    private String category;
    private String color;
    private String material;
    private BigDecimal purchasePrice;
    private BigDecimal salePrice;
    private Integer minStock;
    private Integer totalStock;
    private Boolean isActive;
    private Boolean isLowStock;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    private List<ProductStockResponse> stocks;

    public static ProductResponse fromProduct(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .brand(product.getBrand())
                .category(product.getCategory())
                .color(product.getColor())
                .material(product.getMaterial())
                .purchasePrice(product.getPurchasePrice())
                .salePrice(product.getSalePrice())
                .minStock(product.getMinStock())
                .totalStock(product.getTotalStock())
                .isActive(product.getIsActive())
                .isLowStock(product.isLowStock())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .stocks(product.getStocks().stream()
                        .map(ProductStockResponse::fromProductStock)
                        .collect(Collectors.toList()))
                .build();
    }
}