package com.calzado.sistema_calzado.service.impl;

import com.calzado.sistema_calzado.dto.request.ProviderRequest;
import com.calzado.sistema_calzado.dto.response.ProviderResponse;
import com.calzado.sistema_calzado.model.Provider;
import com.calzado.sistema_calzado.repository.ProviderRepository;
import com.calzado.sistema_calzado.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProviderServiceImpl implements ProviderService {

    @Autowired
    private ProviderRepository providerRepository;

    @Override
    public ProviderResponse createProvider(ProviderRequest request) {
        // Verificar documento único
        if (providerRepository.existsByDocument(request.getDocument())) {
            throw new IllegalArgumentException("El documento del proveedor ya existe");
        }

        Provider provider = new Provider();
        provider.setDocument(request.getDocument());
        provider.setName(request.getName());
        provider.setBusinessName(request.getBusinessName());
        provider.setContactName(request.getContactName());
        provider.setEmail(request.getEmail());
        provider.setPhone(request.getPhone());
        provider.setMobile(request.getMobile());
        provider.setAddress(request.getAddress());
        provider.setCity(request.getCity());
        provider.setCountry(request.getCountry());
        provider.setPaymentTerms(request.getPaymentTerms());
        provider.setPaymentDays(request.getPaymentDays());
        provider.setIsActive(request.getIsActive());
        provider.setNotes(request.getNotes());

        Provider savedProvider = providerRepository.save(provider);
        return ProviderResponse.fromProvider(savedProvider);
    }

    @Override
    public ProviderResponse updateProvider(Long id, ProviderRequest request) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));

        // Verificar documento único si cambió
        if (!provider.getDocument().equals(request.getDocument())
                && providerRepository.existsByDocument(request.getDocument())) {
            throw new IllegalArgumentException("El documento del proveedor ya existe");
        }

        provider.setDocument(request.getDocument());
        provider.setName(request.getName());
        provider.setBusinessName(request.getBusinessName());
        provider.setContactName(request.getContactName());
        provider.setEmail(request.getEmail());
        provider.setPhone(request.getPhone());
        provider.setMobile(request.getMobile());
        provider.setAddress(request.getAddress());
        provider.setCity(request.getCity());
        provider.setCountry(request.getCountry());
        provider.setPaymentTerms(request.getPaymentTerms());
        provider.setPaymentDays(request.getPaymentDays());
        provider.setIsActive(request.getIsActive());
        provider.setNotes(request.getNotes());

        Provider updatedProvider = providerRepository.save(provider);
        return ProviderResponse.fromProvider(updatedProvider);
    }

    @Override
    @Transactional(readOnly = true)
    public ProviderResponse getProviderById(Long id) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));
        return ProviderResponse.fromProvider(provider);
    }

    @Override
    @Transactional(readOnly = true)
    public ProviderResponse getProviderByDocument(String document) {
        Provider provider = providerRepository.findByDocument(document)
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));
        return ProviderResponse.fromProvider(provider);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProviderResponse> getAllProviders() {
        return providerRepository.findAll().stream()
                .map(ProviderResponse::fromProvider)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProviderResponse> getAllProvidersPaginated(Pageable pageable) {
        return providerRepository.findAll(pageable)
                .map(ProviderResponse::fromProvider);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProviderResponse> getActiveProviders() {
        return providerRepository.findByIsActive(true).stream()
                .map(ProviderResponse::fromProvider)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProviderResponse> searchProviders(String search) {
        return providerRepository.searchProviders(search).stream()
                .map(ProviderResponse::fromProvider)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllCities() {
        return providerRepository.findAllCities();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllCountries() {
        return providerRepository.findAllCountries();
    }

    @Override
    public void deleteProvider(Long id) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));
        provider.setIsActive(false);
        providerRepository.save(provider);
    }

    @Override
    public void activateProvider(Long id) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));
        provider.setIsActive(true);
        providerRepository.save(provider);
    }
}