package com.calzado.sistema_calzado.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sale_number", unique = true, nullable = false, length = 50)
    private String saleNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "customer_name", length = 100)
    private String customerName;

    @Column(name = "customer_document", length = 20)
    private String customerDocument;

    @Column(name = "customer_phone", length = 20)
    private String customerPhone;

    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal subtotal;

    @Column(name = "discount", precision = 10, scale = 2)
    @DecimalMin("0.0")
    private BigDecimal discount = BigDecimal.ZERO;

    @Column(name = "tax", precision = 10, scale = 2)
    @DecimalMin("0.0")
    private BigDecimal tax = BigDecimal.ZERO;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal total;

    // CAMPO DEPRECADO - Mantener por compatibilidad con datos antiguos
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", length = 20)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private SaleStatus status = SaleStatus.COMPLETED;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<SaleItem> items = new ArrayList<>();

    // NUEVA RELACIÓN: Pagos múltiples
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<SalePayment> payments = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = SaleStatus.COMPLETED;
        if (discount == null) discount = BigDecimal.ZERO;
        if (tax == null) tax = BigDecimal.ZERO;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addItem(SaleItem item) {
        items.add(item);
        item.setSale(this);
    }

    public void addPayment(SalePayment payment) {
        payments.add(payment);
        payment.setSale(this);
    }

    public void calculateTotals() {
        this.subtotal = items.stream()
                .map(SaleItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.total = this.subtotal.subtract(this.discount).add(this.tax);
    }

    public Integer getTotalItems() {
        return items.stream()
                .mapToInt(SaleItem::getQuantity)
                .sum();
    }

    // Determinar si la venta usa pago mixto
    public boolean isMixedPayment() {
        return payments.size() > 1;
    }

    // Obtener el método de pago principal (para compatibilidad)
    public PaymentMethod getPrimaryPaymentMethod() {
        if (payments.isEmpty()) {
            return paymentMethod; // Datos antiguos
        }
        return payments.get(0).getPaymentMethod();
    }
}