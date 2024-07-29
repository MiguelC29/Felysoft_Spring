package com.felysoft.felysoftApp.service;

import com.felysoft.felysoftApp.entity.Charge;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChargeService {
    List<Charge> findAll() throws Exception;

    Charge findById(Long id);

    @Transactional
    void create(Charge charge);

    @Transactional
    @Modifying
    void update(Charge charge);

    @Transactional
    @Modifying
    void delete(Charge charge);
}
