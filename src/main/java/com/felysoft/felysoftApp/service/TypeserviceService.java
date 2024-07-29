package com.felysoft.felysoftApp.service;

import com.felysoft.felysoftApp.entity.TypeService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TypeserviceService {
    List<TypeService> findAll() throws Exception;

    TypeService findById(Long id);

    @Transactional
    void create(TypeService typeService);

    @Transactional
    @Modifying
    void update(TypeService typeService);

    @Transactional
    @Modifying
    void delete(TypeService typeService);
}
