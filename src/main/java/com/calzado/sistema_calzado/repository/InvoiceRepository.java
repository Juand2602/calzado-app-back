package com.calzado.sistema_calzado.repository;

import com.calzado.sistema_calzado.model.Invoice;
import com.calzado.sistema_calzado.model.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
    boolean existsByInvoiceNumber(String invoiceNumber);

    List<Invoice> findByStatus(PaymentStatus status);
    Page<Invoice> findByStatus(PaymentStatus status, Pageable pageable);

    List<Invoice> findByProviderId(Long providerId);

    @Query("SELECT i FROM Invoice i WHERE i.dueDate < :date AND i.status != 'PAID'")
    List<Invoice> findOverdueInvoices(@Param("date") LocalDate date);

    @Query("SELECT i FROM Invoice i WHERE i.issueDate BETWEEN :startDate AND :endDate")
    List<Invoice> findByDateRange(@Param("startDate") LocalDate startDate,
                                  @Param("endDate") LocalDate endDate);

    @Query("SELECT COALESCE(SUM(i.total), 0) FROM Invoice i WHERE i.status = :status")
    BigDecimal getTotalByStatus(@Param("status") PaymentStatus status);

    @Query("SELECT COALESCE(SUM(i.balance), 0) FROM Invoice i WHERE i.status != 'PAID'")
    BigDecimal getTotalPendingBalance();
}