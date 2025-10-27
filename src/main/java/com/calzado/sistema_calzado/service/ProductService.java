package com.calzado.sistema_calzado.service;

import com.calzado.sistema_calzado.dto.request.ProductRequest;
import com.calzado.sistema_calzado.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse updateProduct(Long id, ProductRequest request);
    ProductResponse getProductById(Long id);
    List<ProductResponse> getAllProducts();
    Page<ProductResponse> getAllProductsPaginated(Pageable pageable);
    List<ProductResponse> getActiveProducts();
    List<ProductResponse> searchProducts(String search);
    List<ProductResponse> getLowStockProducts();
    List<String> getAllCategories();
    List<String> getAllBrands();
    void deleteProduct(Long id);
    void activateProduct(Long id);
    // Agregar estos métodos a la interfaz ProductService.java

    List<String> getAllMaterials();
    List<String> getAllColors();
}