package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Inventory;
import com.felysoft.felysoftApp.entities.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InventoryService {
    public List<Inventory> findAll() throws Exception;
    public Inventory findById(Long id);
    public List<Inventory> findByTypeInv(Inventory.TypeInv typeInv);
    public Inventory findByProduct(Product product);

    @Transactional
    public void create(Inventory inventory);

    @Transactional
    @Modifying
    public void update(Inventory inventory);

    @Transactional
    @Modifying
    public void delete(Inventory inventory);
}