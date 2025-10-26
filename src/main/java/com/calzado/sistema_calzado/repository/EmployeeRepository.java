package com.calzado.sistema_calzado.repository;

import com.calzado.sistema_calzado.model.Employee;
import com.calzado.sistema_calzado.model.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByDocument(String document);
    boolean existsByDocument(String document);

    // Agregado para la funcionalidad de crear usuario
    Optional<Employee> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<Employee> findByUserId(Long userId);

    List<Employee> findByStatus(EmployeeStatus status);
    Page<Employee> findByStatus(EmployeeStatus status, Pageable pageable);

    List<Employee> findByDepartment(String department);
    List<Employee> findByPosition(String position);
    List<Employee> findByCity(String city);

    @Query("SELECT e FROM Employee e WHERE LOWER(e.firstName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(e.document) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(e.email) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Employee> searchEmployees(@Param("search") String search);

    @Query("SELECT DISTINCT e.department FROM Employee e WHERE e.status = 'ACTIVE' AND e.department IS NOT NULL ORDER BY e.department")
    List<String> findAllDepartments();

    @Query("SELECT DISTINCT e.position FROM Employee e WHERE e.status = 'ACTIVE' AND e.position IS NOT NULL ORDER BY e.position")
    List<String> findAllPositions();

    @Query("SELECT COUNT(e) FROM Employee e WHERE e.status = :status")
    Long countByStatus(@Param("status") EmployeeStatus status);
}