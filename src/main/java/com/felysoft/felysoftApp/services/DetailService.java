package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Detail;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DetailService {
    public List<Detail> findAll() throws Exception;
    public Detail findById(Long id);

    @Transactional
    public void create(Detail detail);

    @Transactional
    @Modifying
    public void update(Detail detail);

    @Transactional
    @Modifying
    public void delete(Detail detail);
}