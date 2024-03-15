package com.felysoft.felysoftApp.repositories;

import com.felysoft.felysoftApp.entities.Purchase;
import com.felysoft.felysoftApp.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    List<Purchase> findPurchaseByEliminatedFalse();

    Purchase findPurchaseByIdPurchaseAndEliminatedFalse(Long id);
}