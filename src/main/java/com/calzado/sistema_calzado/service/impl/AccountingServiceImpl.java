package com.calzado.sistema_calzado.service.impl;

import com.calzado.sistema_calzado.dto.request.InvoiceRequest;
import com.calzado.sistema_calzado.dto.request.PaymentRequest;
import com.calzado.sistema_calzado.dto.response.InvoiceResponse;
import com.calzado.sistema_calzado.dto.response.PaymentResponse;
import com.calzado.sistema_calzado.model.*;
import com.calzado.sistema_calzado.repository.*;
import com.calzado.sistema_calzado.service.AccountingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountingServiceImpl implements AccountingService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private UserRepository userRepository;

    // ===== INVOICE METHODS =====

    @Override
    public InvoiceResponse createInvoice(InvoiceRequest request) {
        // Verificar número de factura único
        if (invoiceRepository.existsByInvoiceNumber(request.getInvoiceNumber())) {
            throw new IllegalArgumentException("El número de factura ya existe");
        }

        // Obtener proveedor
        Provider provider = providerRepository.findById(request.getProviderId())
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(request.getInvoiceNumber());
        invoice.setProvider(provider);
        invoice.setIssueDate(request.getIssueDate());
        invoice.setDueDate(request.getDueDate());
        invoice.setSubtotal(request.getSubtotal());
        invoice.setTax(request.getTax());
        invoice.setDiscount(request.getDiscount());
        invoice.setTotal(request.getTotal());
        invoice.setDescription(request.getDescription());
        invoice.setNotes(request.getNotes());

        Invoice savedInvoice = invoiceRepository.save(invoice);
        return InvoiceResponse.fromInvoice(savedInvoice);
    }

    @Override
    public InvoiceResponse updateInvoice(Long id, InvoiceRequest request) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada"));

        // Verificar número único si cambió
        if (!invoice.getInvoiceNumber().equals(request.getInvoiceNumber())
                && invoiceRepository.existsByInvoiceNumber(request.getInvoiceNumber())) {
            throw new IllegalArgumentException("El número de factura ya existe");
        }

        // No permitir editar si ya tiene pagos
        if (!invoice.getPayments().isEmpty()) {
            throw new IllegalArgumentException("No se puede editar una factura con pagos registrados");
        }

        Provider provider = providerRepository.findById(request.getProviderId())
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));

        invoice.setInvoiceNumber(request.getInvoiceNumber());
        invoice.setProvider(provider);
        invoice.setIssueDate(request.getIssueDate());
        invoice.setDueDate(request.getDueDate());
        invoice.setSubtotal(request.getSubtotal());
        invoice.setTax(request.getTax());
        invoice.setDiscount(request.getDiscount());
        invoice.setTotal(request.getTotal());
        invoice.setDescription(request.getDescription());
        invoice.setNotes(request.getNotes());

        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return InvoiceResponse.fromInvoice(updatedInvoice);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada"));
        return InvoiceResponse.fromInvoice(invoice);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponse> getAllInvoices() {
        return invoiceRepository.findAll().stream()
                .map(InvoiceResponse::fromInvoice)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceResponse> getAllInvoicesPaginated(Pageable pageable) {
        return invoiceRepository.findAll(pageable)
                .map(InvoiceResponse::fromInvoice);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponse> getInvoicesByStatus(PaymentStatus status) {
        return invoiceRepository.findByStatus(status).stream()
                .map(InvoiceResponse::fromInvoice)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponse> getInvoicesByProvider(Long providerId) {
        return invoiceRepository.findByProviderId(providerId).stream()
                .map(InvoiceResponse::fromInvoice)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponse> getOverdueInvoices() {
        return invoiceRepository.findOverdueInvoices(LocalDate.now()).stream()
                .map(InvoiceResponse::fromInvoice)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada"));

        // No permitir eliminar si tiene pagos
        if (!invoice.getPayments().isEmpty()) {
            throw new IllegalArgumentException("No se puede eliminar una factura con pagos registrados");
        }

        invoiceRepository.delete(invoice);
    }

    // ===== PAYMENT METHODS =====

    @Override
    public PaymentResponse createPayment(PaymentRequest request, Long userId) {
        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada"));

        // Verificar que no se pague más del saldo
        if (request.getAmount().compareTo(invoice.getBalance()) > 0) {
            throw new IllegalArgumentException("El monto excede el saldo pendiente de la factura");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Payment payment = new Payment();
        payment.setInvoice(invoice);
        payment.setPaymentDate(request.getPaymentDate());
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setReferenceNumber(request.getReferenceNumber());
        payment.setNotes(request.getNotes());
        payment.setUser(user);
        payment.setPaymentNumber(generatePaymentNumber());

        // Agregar pago a la factura
        invoice.addPayment(payment);

        invoiceRepository.save(invoice);

        return PaymentResponse.fromPayment(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByInvoice(Long invoiceId) {
        return paymentRepository.findByInvoiceId(invoiceId).stream()
                .map(PaymentResponse::fromPayment)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(PaymentResponse::fromPayment)
                .collect(Collectors.toList());
    }

    // ===== REPORT METHODS =====

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getFinancialSummary() {
        Map<String, Object> summary = new HashMap<>();

        summary.put("totalPending", invoiceRepository.getTotalByStatus(PaymentStatus.PENDING));
        summary.put("totalPartial", invoiceRepository.getTotalByStatus(PaymentStatus.PARTIAL));
        summary.put("totalPaid", invoiceRepository.getTotalByStatus(PaymentStatus.PAID));
        summary.put("totalOverdue", invoiceRepository.getTotalByStatus(PaymentStatus.OVERDUE));
        summary.put("totalPendingBalance", invoiceRepository.getTotalPendingBalance());

        long countPending = invoiceRepository.findByStatus(PaymentStatus.PENDING).size();
        long countOverdue = invoiceRepository.findByStatus(PaymentStatus.OVERDUE).size();

        summary.put("countPending", countPending);
        summary.put("countOverdue", countOverdue);

        return summary;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalPendingBalance() {
        return invoiceRepository.getTotalPendingBalance();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalPaymentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return paymentRepository.getTotalPaymentsByDateRange(startDate, endDate);
    }

    private String generatePaymentNumber() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = paymentRepository.count() + 1;
        return String.format("PAG-%s-%05d", date, count);
    }
}