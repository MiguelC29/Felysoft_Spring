package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Purchase;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PurchaseService {
    public List<Purchase> findAll() throws Exception;
    public Purchase findById(Long id);

    @Transactional
    public void create(Purchase purchase);

    @Transactional
    @Modifying
    public void update(Purchase purchase);

    @Transactional
    @Modifying
    public void delete(Purchase purchase);
}