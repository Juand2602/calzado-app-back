package com.calzado.sistema_calzado.controller;

import com.calzado.sistema_calzado.dto.request.SaleRequest;
import com.calzado.sistema_calzado.dto.response.SaleResponse;
import com.calzado.sistema_calzado.model.User;
import com.calzado.sistema_calzado.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

    // Crear venta
    @PostMapping
    public ResponseEntity<?> createSale(@Valid @RequestBody SaleRequest request,
                                        Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            SaleResponse sale = saleService.createSale(request, user.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(sale);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Obtener venta por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getSaleById(@PathVariable Long id) {
        try {
            SaleResponse sale = saleService.getSaleById(id);
            return ResponseEntity.ok(sale);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener venta por número
    @GetMapping("/number/{saleNumber}")
    public ResponseEntity<?> getSaleBySaleNumber(@PathVariable String saleNumber) {
        try {
            SaleResponse sale = saleService.getSaleBySaleNumber(saleNumber);
            return ResponseEntity.ok(sale);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Listar todas las ventas
    @GetMapping
    public ResponseEntity<List<SaleResponse>> getAllSales() {
        List<SaleResponse> sales = saleService.getAllSales();
        return ResponseEntity.ok(sales);
    }

    // Listar ventas con paginación
    @GetMapping("/paginated")
    public ResponseEntity<Page<SaleResponse>> getAllSalesPaginated(Pageable pageable) {
        Page<SaleResponse> sales = saleService.getAllSalesPaginated(pageable);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/by-date-range")
    public ResponseEntity<List<SaleResponse>> getSalesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<SaleResponse> sales = saleService.getSalesByDateRange(startDate, endDate);
        return ResponseEntity.ok(sales);
    }

    // Buscar ventas
    @GetMapping("/search")
    public ResponseEntity<List<SaleResponse>> searchSales(@RequestParam String q) {
        List<SaleResponse> sales = saleService.searchSales(q);
        return ResponseEntity.ok(sales);
    }

    // Obtener totales por rango de fechas
    @GetMapping("/totals")
    public ResponseEntity<Map<String, Object>> getTotalsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        BigDecimal totalSales = saleService.getTotalSalesByDateRange(startDate, endDate);
        Long count = saleService.countSalesByDateRange(startDate, endDate);

        Map<String, Object> response = new HashMap<>();
        response.put("totalSales", totalSales);
        response.put("count", count);
        response.put("startDate", startDate);
        response.put("endDate", endDate);

        return ResponseEntity.ok(response);
    }

    // Cancelar venta
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<?> cancelSale(@PathVariable Long id) {
        try {
            saleService.cancelSale(id);
            return ResponseEntity.ok(Map.of("message", "Venta cancelada exitosamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}