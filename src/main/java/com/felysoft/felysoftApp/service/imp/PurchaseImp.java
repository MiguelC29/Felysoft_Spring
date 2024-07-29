package com.felysoft.felysoftApp.service.imp;

import com.felysoft.felysoftApp.entity.Detail;
import com.felysoft.felysoftApp.entity.Purchase;
import com.felysoft.felysoftApp.repository.DetailRepository;
import com.felysoft.felysoftApp.repository.PurchaseRepository;
import com.felysoft.felysoftApp.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseImp implements PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private DetailRepository detailRepository;

    @Override
    public List<Purchase> findAll() throws Exception {
        return this.purchaseRepository.findPurchaseByEliminatedFalse();
    }

    @Override
    public Purchase findById(Long id) {
        return this.purchaseRepository.findPurchaseByIdPurchaseAndEliminatedFalse(id);
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
        this.purchaseRepository.save(purchase);
    }

    @Override
    public void addDetailToPurchase(Long purchaseId, Long detailId) {
        Purchase purchase = this.purchaseRepository.findById(purchaseId).orElse(null);
        Detail detail = this.detailRepository.findById(detailId).orElse(null);

        purchase.getDetails().add(detail);
        this.purchaseRepository.save(purchase);
    }
}
