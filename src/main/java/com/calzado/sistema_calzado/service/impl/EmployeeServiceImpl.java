package com.calzado.sistema_calzado.service.impl;

import com.calzado.sistema_calzado.dto.request.EmployeeRequest;
import com.calzado.sistema_calzado.dto.response.EmployeeResponse;
import com.calzado.sistema_calzado.model.Employee;
import com.calzado.sistema_calzado.model.EmployeeStatus;
import com.calzado.sistema_calzado.model.User;
import com.calzado.sistema_calzado.repository.EmployeeRepository;
import com.calzado.sistema_calzado.repository.UserRepository;
import com.calzado.sistema_calzado.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        // Verificar documento único
        if (employeeRepository.existsByDocument(request.getDocument())) {
            throw new IllegalArgumentException("El documento del empleado ya existe");
        }

        Employee employee = new Employee();
        employee.setDocument(request.getDocument());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setBirthDate(request.getBirthDate());
        employee.setGender(request.getGender());
        employee.setMaritalStatus(request.getMaritalStatus());
        employee.setAddress(request.getAddress());
        employee.setCity(request.getCity());
        employee.setDepartment(request.getDepartment());
        employee.setPhone(request.getPhone());
        employee.setEmail(request.getEmail());
        employee.setPosition(request.getPosition());
        employee.setHireDate(request.getHireDate());
        employee.setSalary(request.getSalary());
        employee.setWorkSchedule(request.getWorkSchedule());
        employee.setContractType(request.getContractType());
        employee.setEmergencyContactName(request.getEmergencyContactName());
        employee.setEmergencyContactPhone(request.getEmergencyContactPhone());
        employee.setEmergencyContactRelationship(request.getEmergencyContactRelationship());
        employee.setStatus(request.getStatus());
        employee.setNotes(request.getNotes());

        // Vincular usuario si se proporciona
        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            employee.setUser(user);
        }

        Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeResponse.fromEmployee(savedEmployee);
    }

    @Override
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));

        // Verificar documento único si cambió
        if (!employee.getDocument().equals(request.getDocument())
                && employeeRepository.existsByDocument(request.getDocument())) {
            throw new IllegalArgumentException("El documento del empleado ya existe");
        }

        employee.setDocument(request.getDocument());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setBirthDate(request.getBirthDate());
        employee.setGender(request.getGender());
        employee.setMaritalStatus(request.getMaritalStatus());
        employee.setAddress(request.getAddress());
        employee.setCity(request.getCity());
        employee.setDepartment(request.getDepartment());
        employee.setPhone(request.getPhone());
        employee.setEmail(request.getEmail());
        employee.setPosition(request.getPosition());
        employee.setHireDate(request.getHireDate());
        employee.setSalary(request.getSalary());
        employee.setWorkSchedule(request.getWorkSchedule());
        employee.setContractType(request.getContractType());
        employee.setEmergencyContactName(request.getEmergencyContactName());
        employee.setEmergencyContactPhone(request.getEmergencyContactPhone());
        employee.setEmergencyContactRelationship(request.getEmergencyContactRelationship());
        employee.setStatus(request.getStatus());
        employee.setNotes(request.getNotes());

        // Actualizar usuario si se proporciona
        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            employee.setUser(user);
        } else {
            employee.setUser(null);
        }

        Employee updatedEmployee = employeeRepository.save(employee);
        return EmployeeResponse.fromEmployee(updatedEmployee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));
        return EmployeeResponse.fromEmployee(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeByDocument(String document) {
        Employee employee = employeeRepository.findByDocument(document)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));
        return EmployeeResponse.fromEmployee(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(EmployeeResponse::fromEmployee)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponse> getAllEmployeesPaginated(Pageable pageable) {
        return employeeRepository.findAll(pageable)
                .map(EmployeeResponse::fromEmployee);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getEmployeesByStatus(EmployeeStatus status) {
        return employeeRepository.findByStatus(status).stream()
                .map(EmployeeResponse::fromEmployee)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> searchEmployees(String search) {
        return employeeRepository.searchEmployees(search).stream()
                .map(EmployeeResponse::fromEmployee)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllDepartments() {
        return employeeRepository.findAllDepartments();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllPositions() {
        return employeeRepository.findAllPositions();
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByStatus(EmployeeStatus status) {
        return employeeRepository.countByStatus(status);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));
        employee.setStatus(EmployeeStatus.INACTIVE);
        employeeRepository.save(employee);
    }

    @Override
    public void changeEmployeeStatus(Long id, EmployeeStatus status) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));
        employee.setStatus(status);
        employeeRepository.save(employee);
    }
}