package com.felysoft.felysoftApp.repository;

import com.felysoft.felysoftApp.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Permission findByName(String name);
}
