package com.calzado.sistema_calzado.dto.response;

import com.calzado.sistema_calzado.model.PaymentMethod;
import com.calzado.sistema_calzado.model.Sale;
import com.calzado.sistema_calzado.model.SaleStatus;
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
public class SaleResponse {

    private Long id;
    private String saleNumber;
    private String userName;
    private String customerName;
    private String customerDocument;
    private String customerPhone;
    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal total;
    private PaymentMethod paymentMethod;
    private SaleStatus status;
    private String notes;
    private Integer totalItems;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    private List<SaleItemResponse> items;

    public static SaleResponse fromSale(Sale sale) {
        return SaleResponse.builder()
                .id(sale.getId())
                .saleNumber(sale.getSaleNumber())
                .userName(sale.getUser().getUsername())
                .customerName(sale.getCustomerName())
                .customerDocument(sale.getCustomerDocument())
                .customerPhone(sale.getCustomerPhone())
                .subtotal(sale.getSubtotal())
                .discount(sale.getDiscount())
                .tax(sale.getTax())
                .total(sale.getTotal())
                .paymentMethod(sale.getPaymentMethod())
                .status(sale.getStatus())
                .notes(sale.getNotes())
                .totalItems(sale.getTotalItems())
                .createdAt(sale.getCreatedAt())
                .updatedAt(sale.getUpdatedAt())
                .items(sale.getItems().stream()
                        .map(SaleItemResponse::fromSaleItem)
                        .collect(Collectors.toList()))
                .build();
    }
}