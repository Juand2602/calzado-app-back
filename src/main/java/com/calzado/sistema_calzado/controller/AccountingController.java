package com.calzado.sistema_calzado.controller;

import com.calzado.sistema_calzado.dto.request.InvoiceRequest;
import com.calzado.sistema_calzado.dto.request.PaymentRequest;
import com.calzado.sistema_calzado.dto.response.InvoiceResponse;
import com.calzado.sistema_calzado.dto.response.PaymentResponse;
import com.calzado.sistema_calzado.model.PaymentStatus;
import com.calzado.sistema_calzado.model.User;
import com.calzado.sistema_calzado.service.AccountingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/accounting")
@PreAuthorize("hasRole('ADMIN')") // Todo el módulo solo para ADMIN
public class AccountingController {

    @Autowired
    private AccountingService accountingService;

    // ===== INVOICE ENDPOINTS =====

    @PostMapping("/invoices")
    public ResponseEntity<?> createInvoice(@Valid @RequestBody InvoiceRequest request) {
        try {
            InvoiceResponse invoice = accountingService.createInvoice(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(invoice);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/invoices/{id}")
    public ResponseEntity<?> updateInvoice(@PathVariable Long id,
                                           @Valid @RequestBody InvoiceRequest request) {
        try {
            InvoiceResponse invoice = accountingService.updateInvoice(id, request);
            return ResponseEntity.ok(invoice);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/invoices/{id}")
    public ResponseEntity<?> getInvoiceById(@PathVariable Long id) {
        try {
            InvoiceResponse invoice = accountingService.getInvoiceById(id);
            return ResponseEntity.ok(invoice);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/invoices")
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices() {
        List<InvoiceResponse> invoices = accountingService.getAllInvoices();
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/invoices/paginated")
    public ResponseEntity<Page<InvoiceResponse>> getAllInvoicesPaginated(Pageable pageable) {
        Page<InvoiceResponse> invoices = accountingService.getAllInvoicesPaginated(pageable);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/invoices/status/{status}")
    public ResponseEntity<List<InvoiceResponse>> getInvoicesByStatus(@PathVariable PaymentStatus status) {
        List<InvoiceResponse> invoices = accountingService.getInvoicesByStatus(status);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/invoices/provider/{providerId}")
    public ResponseEntity<List<InvoiceResponse>> getInvoicesByProvider(@PathVariable Long providerId) {
        List<InvoiceResponse> invoices = accountingService.getInvoicesByProvider(providerId);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/invoices/overdue")
    public ResponseEntity<List<InvoiceResponse>> getOverdueInvoices() {
        List<InvoiceResponse> invoices = accountingService.getOverdueInvoices();
        return ResponseEntity.ok(invoices);
    }

    @DeleteMapping("/invoices/{id}")
    public ResponseEntity<?> deleteInvoice(@PathVariable Long id) {
        try {
            accountingService.deleteInvoice(id);
            return ResponseEntity.ok(Map.of("message", "Factura eliminada exitosamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ===== PAYMENT ENDPOINTS =====

    @PostMapping("/payments")
    public ResponseEntity<?> createPayment(@Valid @RequestBody PaymentRequest request,
                                           Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            PaymentResponse payment = accountingService.createPayment(request, user.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(payment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/payments/invoice/{invoiceId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByInvoice(@PathVariable Long invoiceId) {
        List<PaymentResponse> payments = accountingService.getPaymentsByInvoice(invoiceId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/payments")
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        List<PaymentResponse> payments = accountingService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    // ===== REPORT ENDPOINTS =====

    @GetMapping("/reports/summary")
    public ResponseEntity<Map<String, Object>> getFinancialSummary() {
        Map<String, Object> summary = accountingService.getFinancialSummary();
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/reports/pending-balance")
    public ResponseEntity<Map<String, BigDecimal>> getTotalPendingBalance() {
        BigDecimal balance = accountingService.getTotalPendingBalance();
        return ResponseEntity.ok(Map.of("totalPendingBalance", balance));
    }

    @GetMapping("/reports/payments-by-date")
    public ResponseEntity<Map<String, Object>> getTotalPaymentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        BigDecimal total = accountingService.getTotalPaymentsByDateRange(startDate, endDate);

        return ResponseEntity.ok(Map.of(
                "startDate", startDate,
                "endDate", endDate,
                "totalPayments", total
        ));
    }
}