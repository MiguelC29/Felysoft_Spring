package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductService {
    public List<Product> findAll() throws Exception;
    public Product findById(Long id);

    @Transactional
    public void create(Product product);

    @Transactional
    @Modifying
    public void update(Product product);

    @Transactional
    @Modifying
    public void delete(Product product);
}