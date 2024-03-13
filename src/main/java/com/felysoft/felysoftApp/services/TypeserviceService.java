package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.TypeService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TypeserviceService {
    public List<TypeService> findAll() throws Exception;
    public TypeService findById(Long id);

    @Transactional
    public void create(TypeService typeService);

    @Transactional
    @Modifying
    public void update(TypeService typeService);

    @Transactional
    @Modifying
    public void delete(TypeService typeService);
}