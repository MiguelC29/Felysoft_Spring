package com.felysoft.felysoftApp.service.imp;

import com.felysoft.felysoftApp.entity.Role;
import com.felysoft.felysoftApp.repository.RoleRepository;
import com.felysoft.felysoftApp.service.RoleService;
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
        return this.roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
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
    public void delete(Long id) {
        this.roleRepository.deleteById(id);
    }
}
