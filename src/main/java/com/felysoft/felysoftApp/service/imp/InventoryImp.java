package com.felysoft.felysoftApp.service.imp;

import com.felysoft.felysoftApp.entity.Book;
import com.felysoft.felysoftApp.entity.Inventory;
import com.felysoft.felysoftApp.entity.Product;
import com.felysoft.felysoftApp.repository.InventoryRepository;
import com.felysoft.felysoftApp.service.InventoryService;
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
