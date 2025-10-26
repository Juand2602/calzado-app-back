package com.calzado.sistema_calzado.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProviderRequest {

    @NotBlank(message = "Documento es requerido")
    private String document;

    @NotBlank(message = "Nombre es requerido")
    private String name;

    private String businessName;

    private String contactName;

    @Email(message = "Email debe tener formato válido")
    private String email;

    private String phone;

    private String mobile;

    private String address;

    private String city;

    private String country;

    private String paymentTerms;

    private Integer paymentDays;

    private Boolean isActive = true;

    private String notes;
}