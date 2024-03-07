package com.felysoft.felysoftApp.services.imp;

import com.felysoft.felysoftApp.entities.Purchase;
import com.felysoft.felysoftApp.repositories.PurchaseRepository;
import com.felysoft.felysoftApp.services.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseImp implements PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Override
    public List<Purchase> findAll() throws Exception {
        return this.purchaseRepository.findAll();
    }

    @Override
    public Purchase findById(Long id) {
        return this.purchaseRepository.findById(id).orElse(null);
    }

    @Override
    public void create(Purchase purchase) {
        this.purchaseRepository.save(purchase);
    }

    @Override
    public void update(Purchase purchase) {
        this.purchaseRepository.save(purchase);
    }

    @Override
    public void delete(Purchase purchase) {
        this.purchaseRepository.delete(purchase);
    }
}