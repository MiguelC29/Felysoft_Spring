package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Inventory;

import java.util.List;

public interface InventoryService {
    public List<Inventory> findAll() throws Exception;
    public Inventory findById(Long id);
    public void create(Inventory inventory);
    public void update(Inventory inventory);
    public void delete(Inventory inventory);
}