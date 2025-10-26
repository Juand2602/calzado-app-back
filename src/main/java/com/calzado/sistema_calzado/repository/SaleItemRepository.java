package com.calzado.sistema_calzado.repository;

import com.calzado.sistema_calzado.model.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {

    List<SaleItem> findBySaleId(Long saleId);

    @Query("SELECT si FROM SaleItem si WHERE si.product.id = :productId")
    List<SaleItem> findByProductId(@Param("productId") Long productId);

    @Query("SELECT si.product.id, si.productName, SUM(si.quantity) as totalSold " +
            "FROM SaleItem si " +
            "WHERE si.sale.createdAt BETWEEN :startDate AND :endDate " +
            "AND si.sale.status = 'COMPLETED' " +
            "GROUP BY si.product.id, si.productName " +
            "ORDER BY totalSold DESC")
    List<Object[]> findTopSellingProducts(@Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);
}