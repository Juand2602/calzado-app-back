package com.calzado.sistema_calzado.repository;

import com.calzado.sistema_calzado.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByCode(String code);
    boolean existsByCode(String code);

    List<Product> findByIsActive(Boolean isActive);
    Page<Product> findByIsActive(Boolean isActive, Pageable pageable);

    List<Product> findByCategory(String category);
    List<Product> findByBrand(String brand);
    List<Product> findByColor(String color);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(p.code) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Product> searchProducts(@Param("search") String search);

    @Query("SELECT p FROM Product p WHERE p.isActive = true AND " +
            "SIZE(p.stocks) > 0 AND " +
            "(SELECT SUM(s.quantity) FROM ProductStock s WHERE s.product = p) <= p.minStock")
    List<Product> findLowStockProducts();

    @Query("SELECT DISTINCT p.category FROM Product p WHERE p.isActive = true ORDER BY p.category")
    List<String> findAllCategories();

    @Query("SELECT DISTINCT p.brand FROM Product p WHERE p.isActive = true ORDER BY p.brand")
    List<String> findAllBrands();

    // Agregar estos métodos a la interfaz ProductRepository.java

    @Query("SELECT DISTINCT p.material FROM Product p WHERE p.isActive = true ORDER BY p.material")
    List<String> findAllMaterials();

    @Query("SELECT DISTINCT p.color FROM Product p WHERE p.isActive = true ORDER BY p.color")
    List<String> findAllColors();
}