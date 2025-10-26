package com.calzado.sistema_calzado.service;

import com.calzado.sistema_calzado.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse createUserForEmployee(Long employeeId);
}