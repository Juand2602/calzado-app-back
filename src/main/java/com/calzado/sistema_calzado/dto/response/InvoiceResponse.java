package com.calzado.sistema_calzado.dto.response;

import com.calzado.sistema_calzado.model.Invoice;
import com.calzado.sistema_calzado.model.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponse {

    private Long id;
    private String invoiceNumber;
    private Long providerId;
    private String providerName;
    private String providerDocument;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate issueDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal discount;
    private BigDecimal total;
    private BigDecimal paidAmount;
    private BigDecimal balance;
    private PaymentStatus status;
    private String description;
    private String notes;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    private List<PaymentResponse> payments;

    public static InvoiceResponse fromInvoice(Invoice invoice) {
        return InvoiceResponse.builder()
                .id(invoice.getId())
                .invoiceNumber(invoice.getInvoiceNumber())
                .providerId(invoice.getProvider().getId())
                .providerName(invoice.getProvider().getName())
                .providerDocument(invoice.getProvider().getDocument())
                .issueDate(invoice.getIssueDate())
                .dueDate(invoice.getDueDate())
                .subtotal(invoice.getSubtotal())
                .tax(invoice.getTax())
                .discount(invoice.getDiscount())
                .total(invoice.getTotal())
                .paidAmount(invoice.getPaidAmount())
                .balance(invoice.getBalance())
                .status(invoice.getStatus())
                .description(invoice.getDescription())
                .notes(invoice.getNotes())
                .createdAt(invoice.getCreatedAt())
                .updatedAt(invoice.getUpdatedAt())
                .payments(invoice.getPayments().stream()
                        .map(PaymentResponse::fromPayment)
                        .collect(Collectors.toList()))
                .build();
    }
}
