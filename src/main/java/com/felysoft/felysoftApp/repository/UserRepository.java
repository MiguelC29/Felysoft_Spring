package com.felysoft.felysoftApp.repository;

import com.felysoft.felysoftApp.entity.Role;
import com.felysoft.felysoftApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Buscar un usuario por su email, solo si no está eliminado
    Optional<User> findByEmailAndEliminatedFalse(String email);

    // Buscar un usuario por su número de identificación, solo si no está eliminado
    Optional<User> findByNumIdentificationAndEliminatedFalse(Long numIdentification);

    // Obtener una lista de usuarios no eliminados
    List<User> findUsersByEliminatedFalse();

    // Obtener una lista de usuarios eliminados
    List<User> findUsersByEliminatedTrue();

    // Obtener lista de usuarios por rol, solo si no está eliminado
    List<User> findUsersByRoleAndEliminatedFalseAndEnabledTrue(Role role);

    // Buscar un usuario por ID, solo si no está eliminado
    User findUserByIdUserAndEliminatedFalse(Long id);

    // Buscar un usuario por ID, solo si está eliminado
    User findUserByIdUserAndEliminatedTrue(Long id);

    // Buscar un usuario por email y contraseña, solo si no está eliminado
    User findUserByEmailAndPasswordAndEliminatedFalse(String email, String password);

    // Buscar un usuario por número de identificación, solo si está eliminado
    User findUserByNumIdentificationAndEliminatedTrue(Long numIdentification);
}
