package com.felysoft.felysoftApp.repositories;

import com.felysoft.felysoftApp.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findProductsByEliminatedFalse();
    Product findProductByIdProductAndEliminatedFalse(Long id);
}