package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Charge;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChargeService {
    public List<Charge> findAll() throws Exception;
    public Charge findById(Long id);

    @Transactional
    public void create(Charge charge);

    @Transactional
    @Modifying
    public void update(Charge charge);

    @Transactional
    @Modifying
    public void delete(Charge charge);
}