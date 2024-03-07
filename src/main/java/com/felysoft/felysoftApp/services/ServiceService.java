package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Service;

import java.util.List;

public interface ServiceService {
    public List<Service> findAll() throws Exception;
    public Service findById(Long id);
    public void create(Service service);
    public void update(Service service);
    public void delete(Service service);
}