package com.calzado.sistema_calzado.service;

import com.calzado.sistema_calzado.dto.request.EmployeeRequest;
import com.calzado.sistema_calzado.dto.response.EmployeeResponse;
import com.calzado.sistema_calzado.model.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {
    EmployeeResponse createEmployee(EmployeeRequest request);
    EmployeeResponse updateEmployee(Long id, EmployeeRequest request);
    EmployeeResponse getEmployeeById(Long id);
    EmployeeResponse getEmployeeByDocument(String document);
    List<EmployeeResponse> getAllEmployees();
    Page<EmployeeResponse> getAllEmployeesPaginated(Pageable pageable);
    List<EmployeeResponse> getEmployeesByStatus(EmployeeStatus status);
    List<EmployeeResponse> searchEmployees(String search);
    List<String> getAllDepartments();
    List<String> getAllPositions();
    Long countByStatus(EmployeeStatus status);
    void deleteEmployee(Long id);
    void changeEmployeeStatus(Long id, EmployeeStatus status);
}
