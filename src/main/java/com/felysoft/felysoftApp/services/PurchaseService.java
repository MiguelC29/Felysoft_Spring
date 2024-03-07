package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Purchase;
import java.util.List;

public interface PurchaseService {
    public List<Purchase> findAll() throws Exception;
    public Purchase findById(Long id);
    public void create(Purchase purchase);
    public void update(Purchase purchase);
    public void delete(Purchase purchase);
}