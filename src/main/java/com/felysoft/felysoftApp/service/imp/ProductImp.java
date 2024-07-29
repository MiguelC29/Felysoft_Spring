package com.felysoft.felysoftApp.service.imp;

import com.felysoft.felysoftApp.entity.Product;
import com.felysoft.felysoftApp.repository.ProductRepository;
import com.felysoft.felysoftApp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductImp implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() throws Exception {
        return this.productRepository.findProductsByEliminatedFalse();
    }

    @Override
    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return this.productRepository.findProductByIdProductAndEliminatedFalse(id);
    }

    @Override
    public void create(Product product) {
        this.productRepository.save(product);
    }

    @Override
    @Transactional
    @Modifying
    public void update(Product product) {
        this.productRepository.save(product);
    }

    @Override
    @Transactional
    @Modifying
    public void delete(Product product) {
        this.productRepository.save(product);
    }
}
