package com.calzado.sistema_calzado.dto.response;

import com.calzado.sistema_calzado.model.PaymentMethod;
import com.calzado.sistema_calzado.model.SalePayment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalePaymentResponse {

    private Long id;
    private PaymentMethod paymentMethod;
    private BigDecimal amount;
    private String referenceNumber;
    private String notes;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public static SalePaymentResponse fromSalePayment(SalePayment payment) {
        return SalePaymentResponse.builder()
                .id(payment.getId())
                .paymentMethod(payment.getPaymentMethod())
                .amount(payment.getAmount())
                .referenceNumber(payment.getReferenceNumber())
                .notes(payment.getNotes())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}