package com.felysoft.felysoftApp.service;

import com.felysoft.felysoftApp.entity.Book;
import com.felysoft.felysoftApp.entity.Inventory;
import com.felysoft.felysoftApp.entity.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InventoryService {
    List<Inventory> findAll() throws Exception;

    Inventory findById(Long id);

    List<Inventory> findByTypeInv(Inventory.TypeInv typeInv);

    Inventory findByProduct(Product product);

    Inventory findByBook(Book book);

    @Transactional
    void create(Inventory inventory);

    @Transactional
    @Modifying
    void update(Inventory inventory);

    @Transactional
    @Modifying
    void delete(Inventory inventory);
}
