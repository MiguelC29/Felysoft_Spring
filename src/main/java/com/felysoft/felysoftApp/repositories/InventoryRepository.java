package com.felysoft.felysoftApp.repositories;

import com.felysoft.felysoftApp.entities.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findInventoriesByEliminatedFalse();

    List<Inventory> findInventoriesByTypeInv(Inventory.TypeInv typeInv);
    Inventory findInventoryByIdInventoryAndEliminatedFalse(Long id);
}