package com.calzado.sistema_calzado.repository;

import com.calzado.sistema_calzado.model.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {

    Optional<Provider> findByDocument(String document);
    boolean existsByDocument(String document);

    List<Provider> findByIsActive(Boolean isActive);
    Page<Provider> findByIsActive(Boolean isActive, Pageable pageable);

    List<Provider> findByCity(String city);
    List<Provider> findByCountry(String country);

    @Query("SELECT p FROM Provider p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(p.businessName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(p.document) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(p.contactName) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Provider> searchProviders(@Param("search") String search);

    @Query("SELECT DISTINCT p.city FROM Provider p WHERE p.isActive = true AND p.city IS NOT NULL ORDER BY p.city")
    List<String> findAllCities();

    @Query("SELECT DISTINCT p.country FROM Provider p WHERE p.isActive = true AND p.country IS NOT NULL ORDER BY p.country")
    List<String> findAllCountries();
}