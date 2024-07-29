package com.felysoft.felysoftApp.repository;

import com.felysoft.felysoftApp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findProductsByEliminatedFalse();

    Product findProductByIdProductAndEliminatedFalse(Long id);
    /*
    @PreAuthorize("hasAuthority('SAVE_ONE_PRODUCT')")
    Product save(Product product);*/
}
