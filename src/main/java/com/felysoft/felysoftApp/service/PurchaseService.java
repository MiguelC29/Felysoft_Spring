package com.felysoft.felysoftApp.service;

import com.felysoft.felysoftApp.entity.Purchase;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PurchaseService {
    List<Purchase> findAll() throws Exception;

    Purchase findById(Long id);

    @Transactional
    void create(Purchase purchase);

    @Transactional
    @Modifying
    void update(Purchase purchase);

    @Transactional
    @Modifying
    void delete(Purchase purchase);

    @Transactional
    void addDetailToPurchase(Long purchaseId, Long detailId);
}
