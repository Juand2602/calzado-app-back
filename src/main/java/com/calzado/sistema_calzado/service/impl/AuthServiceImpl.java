package com.calzado.sistema_calzado.service.impl;

import com.calzado.sistema_calzado.dto.response.AuthResponse;
import com.calzado.sistema_calzado.exception.BadRequestException;
import com.calzado.sistema_calzado.exception.ResourceNotFoundException;
import com.calzado.sistema_calzado.model.Employee;
import com.calzado.sistema_calzado.model.Role;
import com.calzado.sistema_calzado.model.User;
import com.calzado.sistema_calzado.repository.EmployeeRepository;
import com.calzado.sistema_calzado.repository.UserRepository;
import com.calzado.sistema_calzado.security.JwtUtils;
import com.calzado.sistema_calzado.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    @Transactional
    public AuthResponse createUserForEmployee(Long employeeId) {
        // Buscar el empleado por ID
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con ID: " + employeeId));

        // Verificar si el empleado ya tiene un usuario asociado
        if (employee.getUser() != null) {
            throw new BadRequestException("El empleado ya tiene un usuario de sistema asociado");
        }

        // Verificar si ya existe un usuario con el mismo email
        if (userRepository.existsByEmail(employee.getEmail())) {
            throw new BadRequestException("Ya existe un usuario con el email: " + employee.getEmail());
        }

        // Crear nuevo usuario
        User newUser = new User();
        newUser.setUsername(employee.getEmail()); // Usar email como username
        newUser.setPassword(passwordEncoder.encode(employee.getDocument())); // Usar documento como contraseña temporal
        newUser.setEmail(employee.getEmail());
        newUser.setFullName(employee.getFullName());
        newUser.setRole(Role.EMPLOYEE); // Rol por defecto para empleados

        // Guardar el usuario
        User savedUser = userRepository.save(newUser);

        // Asociar el usuario con el empleado
        employee.setUser(savedUser);
        employeeRepository.save(employee);

        // Generar token JWT usando el método correcto
        String token = jwtUtils.generateTokenFromUsername(savedUser.getUsername());

        // Construir respuesta
        return new AuthResponse(token,
                savedUser.getUsername(),
                savedUser.getFullName(),
                savedUser.getRole().name(),
                savedUser.getEmail());
    }
}