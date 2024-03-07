package com.felysoft.felysoftApp.services.imp;

import com.felysoft.felysoftApp.entities.Role;
import com.felysoft.felysoftApp.repositories.RoleRepository;
import com.felysoft.felysoftApp.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleImp implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> findAll() throws Exception {
        return this.roleRepository.findAll();
    }

    @Override
    public Role findById(Long id) {
        return this.roleRepository.findById(id).orElse(null);
    }

    @Override
    public void create(Role role) {
        this.roleRepository.save(role);
    }

    @Override
    public void update(Role role) {
        this.roleRepository.save(role);
    }

    @Override
    public void delete(Role role) {
        this.roleRepository.delete(role);
    }
}