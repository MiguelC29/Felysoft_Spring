package com.felysoft.felysoftApp.repository;

import com.felysoft.felysoftApp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findProductsByEliminatedFalse();
    List<Product> findProductsByEliminatedTrue();

    Product findProductByIdProductAndEliminatedFalse(Long id);
    Product findProductByIdProductAndEliminatedTrue(Long id);
    Product findProductByNameAndEliminatedTrue(String name);
    /*
    @PreAuthorize("hasAuthority('SAVE_ONE_PRODUCT')")
    Product save(Product product);*/
}
