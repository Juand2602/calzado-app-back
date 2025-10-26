package com.calzado.sistema_calzado.service.impl;

import com.calzado.sistema_calzado.dto.request.ProductRequest;
import com.calzado.sistema_calzado.dto.request.ProductStockRequest;
import com.calzado.sistema_calzado.dto.response.ProductResponse;
import com.calzado.sistema_calzado.model.Product;
import com.calzado.sistema_calzado.model.ProductStock;
import com.calzado.sistema_calzado.repository.ProductRepository;
import com.calzado.sistema_calzado.repository.ProductStockRepository;
import com.calzado.sistema_calzado.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductStockRepository productStockRepository;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        // Verificar código único
        if (productRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("El código del producto ya existe");
        }

        // Crear producto
        Product product = new Product();
        product.setCode(request.getCode());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setBrand(request.getBrand());
        product.setCategory(request.getCategory());
        product.setColor(request.getColor());
        product.setMaterial(request.getMaterial());
        product.setPurchasePrice(request.getPurchasePrice());
        product.setSalePrice(request.getSalePrice());
        product.setMinStock(request.getMinStock());
        product.setIsActive(request.getIsActive());

        // Guardar producto primero
        Product savedProduct = productRepository.save(product);

        // Agregar stocks si vienen en el request
        if (request.getStocks() != null && !request.getStocks().isEmpty()) {
            for (ProductStockRequest stockRequest : request.getStocks()) {
                ProductStock stock = new ProductStock();
                stock.setProduct(savedProduct);
                stock.setSize(stockRequest.getSize());
                stock.setQuantity(stockRequest.getQuantity());
                stock.setReservedQuantity(0);
                savedProduct.addStock(stock);
            }
            savedProduct = productRepository.save(savedProduct);
        }

        return ProductResponse.fromProduct(savedProduct);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        // Verificar código único si cambió
        if (!product.getCode().equals(request.getCode())
                && productRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("El código del producto ya existe");
        }

        // Actualizar campos
        product.setCode(request.getCode());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setBrand(request.getBrand());
        product.setCategory(request.getCategory());
        product.setColor(request.getColor());
        product.setMaterial(request.getMaterial());
        product.setPurchasePrice(request.getPurchasePrice());
        product.setSalePrice(request.getSalePrice());
        product.setMinStock(request.getMinStock());
        product.setIsActive(request.getIsActive());

        // Actualizar stocks si vienen en el request
        if (request.getStocks() != null) {
            // Obtener los stocks existentes como un mapa por talla para fácil acceso
            Map<String, ProductStock> existingStocksBySize = product.getStocks().stream()
                    .collect(Collectors.toMap(ProductStock::getSize, stock -> stock));

            // Limpiar la lista de stocks existentes
            product.getStocks().clear();

            // Procesar cada stock del request
            for (ProductStockRequest stockRequest : request.getStocks()) {
                String size = stockRequest.getSize();

                // Verificar si ya existe un stock para esta talla
                if (existingStocksBySize.containsKey(size)) {
                    // Actualizar el stock existente
                    ProductStock existingStock = existingStocksBySize.get(size);
                    existingStock.setQuantity(stockRequest.getQuantity());
                    // No necesitamos cambiar reservedQuantity aquí
                    product.addStock(existingStock);
                } else {
                    // Crear un nuevo stock para esta talla
                    ProductStock newStock = new ProductStock();
                    newStock.setProduct(product);
                    newStock.setSize(size);
                    newStock.setQuantity(stockRequest.getQuantity());
                    newStock.setReservedQuantity(0);
                    product.addStock(newStock);
                }
            }
        }

        Product updatedProduct = productRepository.save(product);
        return ProductResponse.fromProduct(updatedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        return ProductResponse.fromProduct(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductResponse::fromProduct)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProductsPaginated(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(ProductResponse::fromProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getActiveProducts() {
        return productRepository.findByIsActive(true).stream()
                .map(ProductResponse::fromProduct)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> searchProducts(String search) {
        return productRepository.searchProducts(search).stream()
                .map(ProductResponse::fromProduct)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getLowStockProducts() {
        return productRepository.findLowStockProducts().stream()
                .map(ProductResponse::fromProduct)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllCategories() {
        return productRepository.findAllCategories();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllBrands() {
        return productRepository.findAllBrands();
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        product.setIsActive(false);
        productRepository.save(product);
    }

    @Override
    public void activateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        product.setIsActive(true);
        productRepository.save(product);
    }
}