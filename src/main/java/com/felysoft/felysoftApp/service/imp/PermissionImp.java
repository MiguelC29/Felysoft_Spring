package com.felysoft.felysoftApp.service.imp;

import com.felysoft.felysoftApp.entity.Permission;
import com.felysoft.felysoftApp.repository.PermissionRepository;
import com.felysoft.felysoftApp.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionImp implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public List<Permission> findAll() throws Exception {
        return this.permissionRepository.findAll();
    }

    @Override
    public Permission findById(Long id) {
        return this.permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
    }

    @Override
    public void create(Permission permission) {
        this.permissionRepository.save(permission);
    }

    @Override
    public void update(Permission permission) {
        this.permissionRepository.save(permission);
    }

    @Override
    public void delete(Long id) {
        this.permissionRepository.deleteById(id);
    }
}
