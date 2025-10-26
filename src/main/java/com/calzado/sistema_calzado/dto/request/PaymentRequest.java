package com.calzado.sistema_calzado.dto.request;

import com.calzado.sistema_calzado.model.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PaymentRequest {

    @NotNull(message = "ID de factura es requerido")
    private Long invoiceId;

    @NotNull(message = "Fecha de pago es requerida")
    private LocalDate paymentDate;

    @NotNull(message = "Monto es requerido")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal amount;

    @NotNull(message = "Método de pago es requerido")
    private PaymentMethod paymentMethod;

    private String referenceNumber;
    private String notes;
}