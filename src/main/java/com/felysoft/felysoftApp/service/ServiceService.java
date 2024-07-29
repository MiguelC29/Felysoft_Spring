package com.felysoft.felysoftApp.service;

import com.felysoft.felysoftApp.entity.Service;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ServiceService {
    List<Service> findAll() throws Exception;

    Service findById(Long id);

    @Transactional
    void create(Service service);

    @Transactional
    @Modifying
    void update(Service service);

    @Transactional
    @Modifying
    void delete(Service service);
}
