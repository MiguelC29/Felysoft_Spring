package com.felysoft.felysoftApp.service;

import com.felysoft.felysoftApp.entity.Detail;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DetailService {
    List<Detail> findAll() throws Exception;

    Detail findById(Long id);

    @Transactional
    void create(Detail detail);

    @Transactional
    @Modifying
    void update(Detail detail);

    @Transactional
    @Modifying
    void delete(Detail detail);
}
