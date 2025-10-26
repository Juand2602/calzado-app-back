package com.calzado.sistema_calzado.service;

import com.calzado.sistema_calzado.dto.request.SaleRequest;
import com.calzado.sistema_calzado.dto.response.SaleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface SaleService {
    SaleResponse createSale(SaleRequest request, Long userId);
    SaleResponse getSaleById(Long id);
    SaleResponse getSaleBySaleNumber(String saleNumber);
    List<SaleResponse> getAllSales();
    Page<SaleResponse> getAllSalesPaginated(Pageable pageable);
    List<SaleResponse> getSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<SaleResponse> searchSales(String search);
    BigDecimal getTotalSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    Long countSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    void cancelSale(Long id);
}