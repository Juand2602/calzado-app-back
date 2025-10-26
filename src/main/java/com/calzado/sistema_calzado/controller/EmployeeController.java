package com.calzado.sistema_calzado.controller;

import com.calzado.sistema_calzado.dto.request.EmployeeRequest;
import com.calzado.sistema_calzado.dto.response.EmployeeResponse;
import com.calzado.sistema_calzado.model.EmployeeStatus;
import com.calzado.sistema_calzado.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/employees")
@PreAuthorize("hasRole('ADMIN')") // Todo el módulo solo para ADMIN
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // Crear empleado
    @PostMapping
    public ResponseEntity<?> createEmployee(@Valid @RequestBody EmployeeRequest request) {
        try {
            EmployeeResponse employee = employeeService.createEmployee(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(employee);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Actualizar empleado
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Long id,
                                            @Valid @RequestBody EmployeeRequest request) {
        try {
            EmployeeResponse employee = employeeService.updateEmployee(id, request);
            return ResponseEntity.ok(employee);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Obtener empleado por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long id) {
        try {
            EmployeeResponse employee = employeeService.getEmployeeById(id);
            return ResponseEntity.ok(employee);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener empleado por documento
    @GetMapping("/document/{document}")
    public ResponseEntity<?> getEmployeeByDocument(@PathVariable String document) {
        try {
            EmployeeResponse employee = employeeService.getEmployeeByDocument(document);
            return ResponseEntity.ok(employee);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Listar todos los empleados
    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        List<EmployeeResponse> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    // Listar empleados con paginación
    @GetMapping("/paginated")
    public ResponseEntity<Page<EmployeeResponse>> getAllEmployeesPaginated(Pageable pageable) {
        Page<EmployeeResponse> employees = employeeService.getAllEmployeesPaginated(pageable);
        return ResponseEntity.ok(employees);
    }

    // Listar empleados por estado
    @GetMapping("/status/{status}")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesByStatus(@PathVariable EmployeeStatus status) {
        List<EmployeeResponse> employees = employeeService.getEmployeesByStatus(status);
        return ResponseEntity.ok(employees);
    }

    // Buscar empleados
    @GetMapping("/search")
    public ResponseEntity<List<EmployeeResponse>> searchEmployees(@RequestParam String q) {
        List<EmployeeResponse> employees = employeeService.searchEmployees(q);
        return ResponseEntity.ok(employees);
    }

    // Obtener departamentos
    @GetMapping("/departments")
    public ResponseEntity<List<String>> getAllDepartments() {
        List<String> departments = employeeService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    // Obtener cargos
    @GetMapping("/positions")
    public ResponseEntity<List<String>> getAllPositions() {
        List<String> positions = employeeService.getAllPositions();
        return ResponseEntity.ok(positions);
    }

    // Obtener estadísticas
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getEmployeeStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("active", employeeService.countByStatus(EmployeeStatus.ACTIVE));
        stats.put("inactive", employeeService.countByStatus(EmployeeStatus.INACTIVE));
        stats.put("vacation", employeeService.countByStatus(EmployeeStatus.VACATION));
        stats.put("suspended", employeeService.countByStatus(EmployeeStatus.SUSPENDED));
        stats.put("total", employeeService.getAllEmployees().size());
        return ResponseEntity.ok(stats);
    }

    // Desactivar empleado (cambiar a INACTIVE)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.ok(Map.of("message", "Empleado desactivado exitosamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Cambiar estado del empleado
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> changeEmployeeStatus(@PathVariable Long id,
                                                  @RequestParam EmployeeStatus status) {
        try {
            employeeService.changeEmployeeStatus(id, status);
            return ResponseEntity.ok(Map.of("message", "Estado actualizado exitosamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}