package com.calzado.sistema_calzado.dto.request;

import com.calzado.sistema_calzado.model.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SaleRequest {

    private String customerName;
    private String customerDocument;
    private String customerPhone;

    // CAMPO DEPRECADO - Mantener por compatibilidad
    private PaymentMethod paymentMethod;

    @DecimalMin(value = "0.0", message = "El descuento debe ser mayor o igual a 0")
    private BigDecimal discount = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "El impuesto debe ser mayor o igual a 0")
    private BigDecimal tax = BigDecimal.ZERO;

    private String notes;

    @NotEmpty(message = "Debe incluir al menos un item")
    @Valid
    private List<SaleItemRequest> items;

    // NUEVA PROPIEDAD: Lista de pagos
    @Valid
    private List<SalePaymentRequest> payments;

    // Helper para determinar si usa múltiples pagos
    public boolean hasMixedPayment() {
        return payments != null && payments.size() > 1;
    }

    // Helper para obtener pago único (compatibilidad)
    public SalePaymentRequest getSinglePayment() {
        if (payments != null && !payments.isEmpty()) {
            return payments.get(0);
        }
        return null;
    }
}