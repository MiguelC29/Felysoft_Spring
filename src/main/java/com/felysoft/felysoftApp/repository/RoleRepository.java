package com.felysoft.felysoftApp.repository;

import com.felysoft.felysoftApp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findRolesByEliminatedFalse();

    Role findRoleByIdRoleAndEliminatedFalse(Long id);
}