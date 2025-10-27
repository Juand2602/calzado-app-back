package com.calzado.sistema_calzado.dto.request;

import com.calzado.sistema_calzado.model.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalePaymentRequest {

    @NotNull(message = "Método de pago es requerido")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Monto es requerido")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal amount;

    private String referenceNumber;

    private String notes;
}