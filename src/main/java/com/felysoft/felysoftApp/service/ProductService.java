package com.felysoft.felysoftApp.service;

import com.felysoft.felysoftApp.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll() throws Exception;

    Product findById(Long id);

    void create(Product product);

    void update(Product product);

    void delete(Product product);
}
