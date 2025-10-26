package com.calzado.sistema_calzado.service;

import com.calzado.sistema_calzado.dto.request.ProviderRequest;
import com.calzado.sistema_calzado.dto.response.ProviderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProviderService {
    ProviderResponse createProvider(ProviderRequest request);
    ProviderResponse updateProvider(Long id, ProviderRequest request);
    ProviderResponse getProviderById(Long id);
    ProviderResponse getProviderByDocument(String document);
    List<ProviderResponse> getAllProviders();
    Page<ProviderResponse> getAllProvidersPaginated(Pageable pageable);
    List<ProviderResponse> getActiveProviders();
    List<ProviderResponse> searchProviders(String search);
    List<String> getAllCities();
    List<String> getAllCountries();
    void deleteProvider(Long id);
    void activateProvider(Long id);
}
