package com.felysoft.felysoftApp.repository;

import com.felysoft.felysoftApp.entity.Book;
import com.felysoft.felysoftApp.entity.Inventory;
import com.felysoft.felysoftApp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findInventoriesByEliminatedFalse();

    List<Inventory> findInventoriesByTypeInvAndEliminatedFalse(Inventory.TypeInv typeInv);
    List<Inventory> findInventoriesByTypeInvAndEliminatedFalseAndState(Inventory.TypeInv typeInv, Inventory.State state);

    Inventory findInventoryByProductAndEliminatedFalse(Product product);
    Inventory findInventoryByProductAndEliminatedTrue(Product product);

    Inventory findInventoryByBookAndEliminatedFalse(Book book);
    Inventory findInventoryByBookAndEliminatedTrue(Book book);

    Inventory findInventoryByIdInventoryAndEliminatedFalse(Long id);
}
