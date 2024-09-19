package com.felysoft.felysoftApp.service;

import com.felysoft.felysoftApp.entity.Role;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> findAll() throws Exception;

    Role findById(Long id);

    Optional<Role> findByName(String name);

    @Transactional
    @Modifying
    void create(Role role);

    @Transactional
    @Modifying
    void update(Role role);

    @Transactional
    @Modifying
    void delete(Long id);
}
