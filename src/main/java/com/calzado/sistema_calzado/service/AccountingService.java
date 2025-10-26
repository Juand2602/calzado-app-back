package com.calzado.sistema_calzado.service;

import com.calzado.sistema_calzado.dto.request.InvoiceRequest;
import com.calzado.sistema_calzado.dto.request.PaymentRequest;
import com.calzado.sistema_calzado.dto.response.InvoiceResponse;
import com.calzado.sistema_calzado.dto.response.PaymentResponse;
import com.calzado.sistema_calzado.model.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AccountingService {
    // Invoices
    InvoiceResponse createInvoice(InvoiceRequest request);
    InvoiceResponse updateInvoice(Long id, InvoiceRequest request);
    InvoiceResponse getInvoiceById(Long id);
    List<InvoiceResponse> getAllInvoices();
    Page<InvoiceResponse> getAllInvoicesPaginated(Pageable pageable);
    List<InvoiceResponse> getInvoicesByStatus(PaymentStatus status);
    List<InvoiceResponse> getInvoicesByProvider(Long providerId);
    List<InvoiceResponse> getOverdueInvoices();
    void deleteInvoice(Long id);

    // Payments
    PaymentResponse createPayment(PaymentRequest request, Long userId);
    List<PaymentResponse> getPaymentsByInvoice(Long invoiceId);
    List<PaymentResponse> getAllPayments();

    // Reports
    Map<String, Object> getFinancialSummary();
    BigDecimal getTotalPendingBalance();
    BigDecimal getTotalPaymentsByDateRange(LocalDate startDate, LocalDate endDate);
}