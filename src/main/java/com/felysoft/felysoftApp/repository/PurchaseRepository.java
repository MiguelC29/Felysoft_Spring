package com.felysoft.felysoftApp.repository;

import com.felysoft.felysoftApp.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findPurchaseByEliminatedFalse();
    List<Purchase> findPurchaseByEliminatedTrue();

    Purchase findPurchaseByIdPurchaseAndEliminatedFalse(Long id);
    Purchase findPurchaseByIdPurchaseAndEliminatedTrue(Long id);
}
