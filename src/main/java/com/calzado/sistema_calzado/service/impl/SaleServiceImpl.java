package com.calzado.sistema_calzado.service.impl;

import com.calzado.sistema_calzado.dto.request.SaleItemRequest;
import com.calzado.sistema_calzado.dto.request.SaleRequest;
import com.calzado.sistema_calzado.dto.response.SaleResponse;
import com.calzado.sistema_calzado.model.*;
import com.calzado.sistema_calzado.repository.*;
import com.calzado.sistema_calzado.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SaleServiceImpl implements SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SaleItemRepository saleItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductStockRepository productStockRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public SaleResponse createSale(SaleRequest request, Long userId) {
        // Obtener usuario
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Crear venta
        Sale sale = new Sale();
        sale.setUser(user);
        sale.setCustomerName(request.getCustomerName());
        sale.setCustomerDocument(request.getCustomerDocument());
        sale.setCustomerPhone(request.getCustomerPhone());
        sale.setPaymentMethod(request.getPaymentMethod());
        sale.setDiscount(request.getDiscount());
        sale.setTax(request.getTax());
        sale.setNotes(request.getNotes());
        sale.setStatus(SaleStatus.COMPLETED);

        // Generar número de venta
        sale.setSaleNumber(generateSaleNumber());

        // Procesar items
        for (SaleItemRequest itemRequest : request.getItems()) {
            // Obtener producto
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + itemRequest.getProductId()));

            // Verificar que el producto esté activo
            if (!product.getIsActive()) {
                throw new IllegalArgumentException("El producto " + product.getName() + " no está activo");
            }

            // Obtener stock de la talla
            ProductStock stock = productStockRepository.findByProductIdAndSize(
                            itemRequest.getProductId(), itemRequest.getSize())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "No hay stock para la talla " + itemRequest.getSize() + " del producto " + product.getName()));

            // Verificar stock disponible
            if (stock.getAvailableQuantity() < itemRequest.getQuantity()) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para " + product.getName() + " talla " + itemRequest.getSize() +
                                ". Disponible: " + stock.getAvailableQuantity() + ", Solicitado: " + itemRequest.getQuantity());
            }

            // Descontar del stock
            stock.setQuantity(stock.getQuantity() - itemRequest.getQuantity());
            productStockRepository.save(stock);

            // Crear item de venta
            SaleItem saleItem = new SaleItem();
            saleItem.setProduct(product);
            saleItem.setProductName(product.getName());
            saleItem.setProductCode(product.getCode());
            saleItem.setSize(itemRequest.getSize());
            saleItem.setQuantity(itemRequest.getQuantity());
            saleItem.setUnitPrice(product.getSalePrice());
            saleItem.calculateSubtotal();

            sale.addItem(saleItem);
        }

        // Calcular totales
        sale.calculateTotals();

        // Guardar venta
        Sale savedSale = saleRepository.save(sale);

        return SaleResponse.fromSale(savedSale);
    }

    @Override
    @Transactional(readOnly = true)
    public SaleResponse getSaleById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada"));
        return SaleResponse.fromSale(sale);
    }

    @Override
    @Transactional(readOnly = true)
    public SaleResponse getSaleBySaleNumber(String saleNumber) {
        Sale sale = saleRepository.findBySaleNumber(saleNumber)
                .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada"));
        return SaleResponse.fromSale(sale);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponse> getAllSales() {
        return saleRepository.findAll().stream()
                .map(SaleResponse::fromSale)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SaleResponse> getAllSalesPaginated(Pageable pageable) {
        return saleRepository.findAll(pageable)
                .map(SaleResponse::fromSale);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponse> getSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.findByDateRange(startDate, endDate).stream()
                .map(SaleResponse::fromSale)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponse> searchSales(String search) {
        return saleRepository.searchSales(search).stream()
                .map(SaleResponse::fromSale)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.getTotalSalesByDateRange(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.countSalesByDateRange(startDate, endDate);
    }

    @Override
    public void cancelSale(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada"));

        if (sale.getStatus() == SaleStatus.CANCELLED) {
            throw new IllegalArgumentException("La venta ya está cancelada");
        }

        // Devolver stock
        for (SaleItem item : sale.getItems()) {
            ProductStock stock = productStockRepository
                    .findByProductIdAndSize(item.getProduct().getId(), item.getSize())
                    .orElseThrow(() -> new IllegalArgumentException("Stock no encontrado"));

            stock.setQuantity(stock.getQuantity() + item.getQuantity());
            productStockRepository.save(stock);
        }

        sale.setStatus(SaleStatus.CANCELLED);
        saleRepository.save(sale);
    }

    private String generateSaleNumber() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = saleRepository.count() + 1;
        return String.format("VEN-%s-%05d", date, count);
    }
}
