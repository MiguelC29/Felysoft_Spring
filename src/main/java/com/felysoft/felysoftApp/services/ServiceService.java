package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Service;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ServiceService {
    public List<Service> findAll() throws Exception;
    public Service findById(Long id);

    @Transactional
    public void create(Service service);

    @Transactional
    @Modifying
    public void update(Service service);

    @Transactional
    @Modifying
    public void delete(Service service);
}