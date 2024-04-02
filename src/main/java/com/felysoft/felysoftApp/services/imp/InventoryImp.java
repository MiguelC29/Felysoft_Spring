package com.felysoft.felysoftApp.services.imp;

import com.felysoft.felysoftApp.entities.Book;
import com.felysoft.felysoftApp.entities.Inventory;
import com.felysoft.felysoftApp.entities.Product;
import com.felysoft.felysoftApp.repositories.InventoryRepository;
import com.felysoft.felysoftApp.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryImp implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public List<Inventory> findAll() throws Exception {
        return this.inventoryRepository.findInventoriesByEliminatedFalse();
    }

    @Override
    public Inventory findById(Long id) {
        return this.inventoryRepository.findInventoryByIdInventoryAndEliminatedFalse(id);
    }

    @Override
    public List<Inventory> findByTypeInv(Inventory.TypeInv typeInv) {
        return this.inventoryRepository.findInventoriesByTypeInvAndEliminatedFalse(typeInv);
    }

    @Override
    public Inventory findByProduct(Product product) {
        return this.inventoryRepository.findInventoryByProductAndEliminatedFalse(product);
    }

    @Override
    public Inventory findByBook(Book book) {
        return this.inventoryRepository.findInventoryByBookAndEliminatedFalse(book);
    }

    @Override
    public void create(Inventory inventory) {
        this.inventoryRepository.save(inventory);
    }

    @Override
    public void update(Inventory inventory) {
        this.inventoryRepository.save(inventory);
    }

    @Override
    public void delete(Inventory inventory) {
        this.inventoryRepository.save(inventory);
    }
}