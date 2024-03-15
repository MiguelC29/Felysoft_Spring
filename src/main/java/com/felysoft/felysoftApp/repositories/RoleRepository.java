package com.felysoft.felysoftApp.repositories;

import com.felysoft.felysoftApp.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findRolesByEliminatedFalse();
    Role findRoleByIdRoleAndEliminatedFalse(Long id);
}