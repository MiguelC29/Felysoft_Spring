package com.felysoft.felysoftApp.repository;

import com.felysoft.felysoftApp.entity.Detail;
import com.felysoft.felysoftApp.entity.Purchase;
import com.felysoft.felysoftApp.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailRepository extends JpaRepository <Detail, Long> {
    List<Detail> findDetailByEliminatedFalse();

    List<Detail> findDetailsByPurchaseAndEliminatedFalse(Purchase purchase);
    List<Detail> findDetailsBySaleAndEliminatedFalse(Sale sale);

    Detail findDetailByIdDetailAndEliminatedFalse(Long id);
}
