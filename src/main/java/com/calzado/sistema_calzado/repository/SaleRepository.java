package com.calzado.sistema_calzado.repository;

import com.calzado.sistema_calzado.model.Sale;
import com.calzado.sistema_calzado.model.SaleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    Optional<Sale> findBySaleNumber(String saleNumber);

    List<Sale> findByStatus(SaleStatus status);
    Page<Sale> findByStatus(SaleStatus status, Pageable pageable);

    List<Sale> findByUserId(Long userId);

    @Query("SELECT s FROM Sale s WHERE s.createdAt BETWEEN :startDate AND :endDate ORDER BY s.createdAt DESC")
    List<Sale> findByDateRange(@Param("startDate") LocalDateTime startDate,
                               @Param("endDate") LocalDateTime endDate);

    @Query("SELECT s FROM Sale s WHERE s.customerName LIKE %:search% " +
            "OR s.customerDocument LIKE %:search% " +
            "OR s.saleNumber LIKE %:search%")
    List<Sale> searchSales(@Param("search") String search);

    @Query("SELECT COALESCE(SUM(s.total), 0) FROM Sale s WHERE s.status = 'COMPLETED' " +
            "AND s.createdAt BETWEEN :startDate AND :endDate")
    java.math.BigDecimal getTotalSalesByDateRange(@Param("startDate") LocalDateTime startDate,
                                                  @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.status = 'COMPLETED' " +
            "AND s.createdAt BETWEEN :startDate AND :endDate")
    Long countSalesByDateRange(@Param("startDate") LocalDateTime startDate,
                               @Param("endDate") LocalDateTime endDate);
}