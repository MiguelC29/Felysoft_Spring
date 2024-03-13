package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Role;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RoleService {
    public List<Role> findAll() throws Exception;
    public Role findById(Long id);

    @Transactional
    public void create(Role role);

    @Transactional
    @Modifying
    public void update(Role role);

    @Transactional
    @Modifying
    public void delete(Role role);
}