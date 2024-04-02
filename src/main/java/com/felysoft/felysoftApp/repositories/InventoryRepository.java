package com.felysoft.felysoftApp.repositories;

import com.felysoft.felysoftApp.entities.Book;
import com.felysoft.felysoftApp.entities.Inventory;
import com.felysoft.felysoftApp.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findInventoriesByEliminatedFalse();
    List<Inventory> findInventoriesByTypeInvAndEliminatedFalse(Inventory.TypeInv typeInv);
    Inventory findInventoryByProductAndEliminatedFalse(Product product);
    Inventory findInventoryByBookAndEliminatedFalse(Book book);
    Inventory findInventoryByIdInventoryAndEliminatedFalse(Long id);
}