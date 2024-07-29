package com.felysoft.felysoftApp.repository;

import com.felysoft.felysoftApp.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository <Sale, Long> {
    /*@Query("SELECT s FROM Sale s WHERE s.eliminated = false")
    List<Sale>findAll(); */

    List<Sale> findSaleByEliminatedFalse();

    Sale findSaleByIdSaleAndEliminatedFalse(Long id);
}
