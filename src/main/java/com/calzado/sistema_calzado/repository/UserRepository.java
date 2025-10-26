package com.calzado.sistema_calzado.repository;

import com.calzado.sistema_calzado.model.User;
import com.calzado.sistema_calzado.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Métodos básicos de búsqueda
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    // Verificar existencia
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // Búsqueda por estado
    List<User> findByIsActive(Boolean isActive);

    // Búsqueda por rol
    List<User> findByRole(Role role);
    Page<User> findByRole(Role role, Pageable pageable);

    // Búsqueda combinada
    Page<User> findByIsActiveAndRole(Boolean isActive, Role role, Pageable pageable);

    // Búsqueda por nombre (like)
    @Query("SELECT u FROM User u WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> findByFullNameContainingIgnoreCase(@Param("name") String name);

    // Usuarios creados en un rango de fechas
    @Query("SELECT u FROM User u WHERE u.createdAt BETWEEN :startDate AND :endDate")
    List<User> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);

    // Usuarios con último login reciente
    @Query("SELECT u FROM User u WHERE u.lastLogin >= :sinceDate")
    List<User> findRecentlyActive(@Param("sinceDate") LocalDateTime sinceDate);

    // Contar usuarios por rol
    long countByRole(Role role);

    // Contar usuarios activos
    long countByIsActive(Boolean isActive);
}