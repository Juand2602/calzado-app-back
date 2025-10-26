package com.calzado.sistema_calzado.dto.request;

import com.calzado.sistema_calzado.model.EmployeeStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EmployeeRequest {

    // Información Personal
    @NotBlank(message = "Documento es requerido")
    private String document;

    @NotBlank(message = "Nombre es requerido")
    private String firstName;

    @NotBlank(message = "Apellido es requerido")
    private String lastName;

    private LocalDate birthDate;
    private String gender;
    private String maritalStatus;
    private String address;
    private String city;
    private String department;
    private String phone;

    @Email(message = "Email debe tener formato válido")
    private String email;

    // Información Laboral
    private String position;
    private LocalDate hireDate;
    private BigDecimal salary;
    private String workSchedule;
    private String contractType;

    // Contacto de Emergencia
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String emergencyContactRelationship;

    // Estado
    private EmployeeStatus status = EmployeeStatus.ACTIVE;
    private String notes;

    // Usuario asociado (opcional)
    private Long userId;
}