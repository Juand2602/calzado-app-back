package com.calzado.sistema_calzado.dto.response;

import com.calzado.sistema_calzado.model.Provider;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderResponse {

    private Long id;
    private String document;
    private String name;
    private String businessName;
    private String contactName;
    private String email;
    private String phone;
    private String mobile;
    private String address;
    private String city;
    private String country;
    private String paymentTerms;
    private Integer paymentDays;
    private Boolean isActive;
    private String notes;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public static ProviderResponse fromProvider(Provider provider) {
        return ProviderResponse.builder()
                .id(provider.getId())
                .document(provider.getDocument())
                .name(provider.getName())
                .businessName(provider.getBusinessName())
                .contactName(provider.getContactName())
                .email(provider.getEmail())
                .phone(provider.getPhone())
                .mobile(provider.getMobile())
                .address(provider.getAddress())
                .city(provider.getCity())
                .country(provider.getCountry())
                .paymentTerms(provider.getPaymentTerms())
                .paymentDays(provider.getPaymentDays())
                .isActive(provider.getIsActive())
                .notes(provider.getNotes())
                .createdAt(provider.getCreatedAt())
                .updatedAt(provider.getUpdatedAt())
                .build();
    }
}