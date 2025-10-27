package com.calzado.sistema_calzado.repository;

import com.calzado.sistema_calzado.model.SalePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SalePaymentRepository extends JpaRepository<SalePayment, Long> {

    // Buscar pagos por venta
    List<SalePayment> findBySaleId(Long saleId);

    // Obtener total de pagos por rango de fechas y método
    @Query("SELECT SUM(sp.amount) FROM SalePayment sp " +
            "WHERE sp.createdAt BETWEEN :startDate AND :endDate " +
            "AND sp.paymentMethod = :paymentMethod")
    BigDecimal getTotalByDateRangeAndMethod(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("paymentMethod") String paymentMethod
    );

    // Contar pagos por método en un rango de fechas
    @Query("SELECT COUNT(sp) FROM SalePayment sp " +
            "WHERE sp.createdAt BETWEEN :startDate AND :endDate " +
            "AND sp.paymentMethod = :paymentMethod")
    Long countByDateRangeAndMethod(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("paymentMethod") String paymentMethod
    );
}