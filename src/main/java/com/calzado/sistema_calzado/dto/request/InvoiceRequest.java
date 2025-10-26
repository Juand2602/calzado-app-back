package com.calzado.sistema_calzado.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class InvoiceRequest {

    @NotBlank(message = "Número de factura es requerido")
    private String invoiceNumber;

    @NotNull(message = "Proveedor es requerido")
    private Long providerId;

    @NotNull(message = "Fecha de emisión es requerida")
    private LocalDate issueDate;

    @NotNull(message = "Fecha de vencimiento es requerida")
    private LocalDate dueDate;

    @NotNull(message = "Subtotal es requerido")
    @DecimalMin(value = "0.0", message = "El subtotal debe ser mayor o igual a 0")
    private BigDecimal subtotal;

    @DecimalMin(value = "0.0", message = "El impuesto debe ser mayor o igual a 0")
    private BigDecimal tax = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "El descuento debe ser mayor o igual a 0")
    private BigDecimal discount = BigDecimal.ZERO;

    @NotNull(message = "Total es requerido")
    @DecimalMin(value = "0.0", message = "El total debe ser mayor o igual a 0")
    private BigDecimal total;

    private String description;
    private String notes;
}