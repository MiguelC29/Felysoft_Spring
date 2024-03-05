package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Product;

import java.util.List;

public interface ProductService {
    public List<Product> findAll() throws Exception;
    public Product findById(Long id);
    public void create(Product product);
    public void update(Product product);
    public void delete(Product product);
}