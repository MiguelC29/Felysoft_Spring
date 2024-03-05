package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Role;

import java.util.List;

public interface RoleService {
    public List<Role> findAll() throws Exception;
    public void create(Role role);
    public void update(Role role);
    public void delete(Role role);
}