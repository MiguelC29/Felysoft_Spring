package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Sale;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SaleService {
    public List<Sale> findAll() throws Exception;
    public Sale findById(Long id);

    @Transactional
    public void create(Sale sale);

    @Transactional
    @Modifying
    public void update(Sale sale);

    @Transactional
    @Modifying
    public void delete(Sale sale);
}