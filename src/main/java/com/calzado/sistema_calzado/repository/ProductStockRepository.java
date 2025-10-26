package com.calzado.sistema_calzado.repository;

import com.calzado.sistema_calzado.model.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {

    List<ProductStock> findByProductId(Long productId);
    Optional<ProductStock> findByProductIdAndSize(Long productId, String size);
    boolean existsByProductIdAndSize(Long productId, String size);
}