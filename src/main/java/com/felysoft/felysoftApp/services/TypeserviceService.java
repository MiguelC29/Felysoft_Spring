package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.TypeService;

import java.util.List;

public interface TypeserviceService {
    public List<TypeService> findAll() throws Exception;
    public TypeService findById(Long id);
    public void create(TypeService typeService);
    public void update(TypeService typeService);
    public void delete(TypeService typeService);
}