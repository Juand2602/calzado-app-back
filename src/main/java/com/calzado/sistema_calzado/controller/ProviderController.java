package com.calzado.sistema_calzado.controller;

import com.calzado.sistema_calzado.dto.request.ProviderRequest;
import com.calzado.sistema_calzado.dto.response.ProviderResponse;
import com.calzado.sistema_calzado.service.ProviderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/providers")
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    // Crear proveedor (Solo ADMIN)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createProvider(@Valid @RequestBody ProviderRequest request) {
        try {
            ProviderResponse provider = providerService.createProvider(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(provider);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Actualizar proveedor (Solo ADMIN)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateProvider(@PathVariable Long id,
                                            @Valid @RequestBody ProviderRequest request) {
        try {
            ProviderResponse provider = providerService.updateProvider(id, request);
            return ResponseEntity.ok(provider);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Obtener proveedor por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getProviderById(@PathVariable Long id) {
        try {
            ProviderResponse provider = providerService.getProviderById(id);
            return ResponseEntity.ok(provider);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener proveedor por documento
    @GetMapping("/document/{document}")
    public ResponseEntity<?> getProviderByDocument(@PathVariable String document) {
        try {
            ProviderResponse provider = providerService.getProviderByDocument(document);
            return ResponseEntity.ok(provider);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Listar todos los proveedores
    @GetMapping
    public ResponseEntity<List<ProviderResponse>> getAllProviders() {
        List<ProviderResponse> providers = providerService.getAllProviders();
        return ResponseEntity.ok(providers);
    }

    // Listar proveedores con paginación
    @GetMapping("/paginated")
    public ResponseEntity<Page<ProviderResponse>> getAllProvidersPaginated(Pageable pageable) {
        Page<ProviderResponse> providers = providerService.getAllProvidersPaginated(pageable);
        return ResponseEntity.ok(providers);
    }

    // Listar proveedores activos
    @GetMapping("/active")
    public ResponseEntity<List<ProviderResponse>> getActiveProviders() {
        List<ProviderResponse> providers = providerService.getActiveProviders();
        return ResponseEntity.ok(providers);
    }

    // Buscar proveedores
    @GetMapping("/search")
    public ResponseEntity<List<ProviderResponse>> searchProviders(@RequestParam String q) {
        List<ProviderResponse> providers = providerService.searchProviders(q);
        return ResponseEntity.ok(providers);
    }

    // Obtener ciudades
    @GetMapping("/cities")
    public ResponseEntity<List<String>> getAllCities() {
        List<String> cities = providerService.getAllCities();
        return ResponseEntity.ok(cities);
    }

    // Obtener países
    @GetMapping("/countries")
    public ResponseEntity<List<String>> getAllCountries() {
        List<String> countries = providerService.getAllCountries();
        return ResponseEntity.ok(countries);
    }

    // Desactivar proveedor (Solo ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProvider(@PathVariable Long id) {
        try {
            providerService.deleteProvider(id);
            return ResponseEntity.ok(Map.of("message", "Proveedor desactivado exitosamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Activar proveedor (Solo ADMIN)
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> activateProvider(@PathVariable Long id) {
        try {
            providerService.activateProvider(id);
            return ResponseEntity.ok(Map.of("message", "Proveedor activado exitosamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}