package com.calzado.sistema_calzado.dto.request;

import com.calzado.sistema_calzado.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequest {

    @NotBlank(message = "Username es requerido")
    @Size(min = 3, max = 50, message = "Username debe tener entre 3 y 50 caracteres")
    private String username;

    @NotBlank(message = "Password es requerido")
    @Size(min = 6, message = "Password debe tener al menos 6 caracteres")
    private String password;

    @Email(message = "Email debe tener formato válido")
    @NotBlank(message = "Email es requerido")
    private String email;

    @NotBlank(message = "Nombre completo es requerido")
    @Size(max = 100, message = "Nombre completo no puede exceder 100 caracteres")
    private String fullName;

    @NotNull(message = "Rol es requerido")
    private Role role = Role.EMPLOYEE;

    private Boolean isActive = true;
}