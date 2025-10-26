package com.calzado.sistema_calzado.dto.response;

import com.calzado.sistema_calzado.model.Employee;
import com.calzado.sistema_calzado.model.EmployeeStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {

    private Long id;

    // Información Personal
    private String document;
    private String firstName;
    private String lastName;
    private String fullName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private String gender;
    private String maritalStatus;
    private String address;
    private String city;
    private String department;
    private String phone;
    private String email;

    // Información Laboral
    private String position;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;

    private BigDecimal salary;
    private String workSchedule;
    private String contractType;

    // Contacto de Emergencia
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String emergencyContactRelationship;

    // Estado
    private EmployeeStatus status;
    private String notes;

    // Usuario asociado
    private Long userId;
    private String username;

    // Auditoría
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public static EmployeeResponse fromEmployee(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .document(employee.getDocument())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .fullName(employee.getFullName())
                .birthDate(employee.getBirthDate())
                .gender(employee.getGender())
                .maritalStatus(employee.getMaritalStatus())
                .address(employee.getAddress())
                .city(employee.getCity())
                .department(employee.getDepartment())
                .phone(employee.getPhone())
                .email(employee.getEmail())
                .position(employee.getPosition())
                .hireDate(employee.getHireDate())
                .salary(employee.getSalary())
                .workSchedule(employee.getWorkSchedule())
                .contractType(employee.getContractType())
                .emergencyContactName(employee.getEmergencyContactName())
                .emergencyContactPhone(employee.getEmergencyContactPhone())
                .emergencyContactRelationship(employee.getEmergencyContactRelationship())
                .status(employee.getStatus())
                .notes(employee.getNotes())
                .userId(employee.getUser() != null ? employee.getUser().getId() : null)
                .username(employee.getUser() != null ? employee.getUser().getUsername() : null)
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .build();
    }
}