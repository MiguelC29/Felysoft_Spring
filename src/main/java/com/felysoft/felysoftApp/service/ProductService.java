package com.felysoft.felysoftApp.service;

import com.felysoft.felysoftApp.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll() throws Exception;

    List<Product> findAllDisabled() throws Exception;

    Product findById(Long id);

    Product findByIdDisabled(Long id);

    Product findProductByNameAndEliminated(String name);

    Product findProductByName(String name);

    List<Product> findByIdProvider(Long id);

    void create(Product product);

    void update(Product product);

    void delete(Product product);
}
