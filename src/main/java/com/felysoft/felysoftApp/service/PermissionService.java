package com.felysoft.felysoftApp.service;

import com.felysoft.felysoftApp.entity.Permission;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PermissionService {
    List<Permission> findAll() throws Exception;

    Permission findById(Long id);

    @Transactional
    @Modifying
    void create(Permission permission);

    @Transactional
    @Modifying
    void update(Permission permission);

    @Transactional
    @Modifying
    void delete(Long id);
}
