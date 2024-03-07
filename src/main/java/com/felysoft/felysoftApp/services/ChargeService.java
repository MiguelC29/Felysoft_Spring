package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Charge;
import java.util.List;

public interface ChargeService {
    public List<Charge> findAll() throws Exception;
    public Charge findById(Long id);
    public void create(Charge charge);
    public void update(Charge charge);
    public void delete(Charge charge);
}