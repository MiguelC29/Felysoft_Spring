package com.felysoft.felysoftApp.services.imp;

import com.felysoft.felysoftApp.entities.Product;
import com.felysoft.felysoftApp.repositories.ProductRepository;
import com.felysoft.felysoftApp.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImp implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> findAll() throws Exception {
        return this.productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        return this.productRepository.findById(id).orElse(null);
    }

    @Override
    public void create(Product product) {
        this.productRepository.save(product);
    }

    @Override
    public void update(Product product) {
        this.productRepository.save(product);
    }

    @Override
    public void delete(Product product) {
        this.productRepository.delete(product);
    }
}