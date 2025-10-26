package com.calzado.sistema_calzado.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_number", unique = true, nullable = false, length = 50)
    @NotBlank(message = "Número de factura es requerido")
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "provider_id", nullable = false)
    @NotNull(message = "Proveedor es requerido")
    private Provider provider;

    @Column(name = "issue_date", nullable = false)
    @NotNull(message = "Fecha de emisión es requerida")
    private LocalDate issueDate;

    @Column(name = "due_date", nullable = false)
    @NotNull(message = "Fecha de vencimiento es requerida")
    private LocalDate dueDate;

    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal subtotal;

    @Column(name = "tax", precision = 10, scale = 2)
    @DecimalMin("0.0")
    private BigDecimal tax = BigDecimal.ZERO;

    @Column(name = "discount", precision = 10, scale = 2)
    @DecimalMin("0.0")
    private BigDecimal discount = BigDecimal.ZERO;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal total;

    @Column(name = "paid_amount", precision = 10, scale = 2)
    @DecimalMin("0.0")
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Column(name = "balance", precision = 10, scale = 2)
    @DecimalMin("0.0")
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Payment> payments = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = PaymentStatus.PENDING;
        if (paidAmount == null) paidAmount = BigDecimal.ZERO;
        if (tax == null) tax = BigDecimal.ZERO;
        if (discount == null) discount = BigDecimal.ZERO;
        calculateBalance();
        updateStatus();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculateBalance();
        updateStatus();
    }

    public void calculateBalance() {
        if (total != null && paidAmount != null) {
            this.balance = total.subtract(paidAmount);
        }
    }

    public void updateStatus() {
        if (balance == null) {
            calculateBalance();
        }

        if (balance.compareTo(BigDecimal.ZERO) == 0) {
            this.status = PaymentStatus.PAID;
        } else if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
            this.status = PaymentStatus.PARTIAL;
        } else if (LocalDate.now().isAfter(dueDate)) {
            this.status = PaymentStatus.OVERDUE;
        } else {
            this.status = PaymentStatus.PENDING;
        }
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setInvoice(this);
        this.paidAmount = this.paidAmount.add(payment.getAmount());
        calculateBalance();
        updateStatus();
    }
}