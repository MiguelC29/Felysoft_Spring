package com.felysoft.felysoftApp.repository;

import com.felysoft.felysoftApp.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    List<Brand> findBrandsByEliminatedFalse();
    List<Brand> findBrandsByEliminatedTrue();

    Brand findBrandByIdBrandAndEliminatedFalse(Long id);
    Brand findBrandByIdBrandAndEliminatedTrue(Long id);
    Brand findBrandByNameAndEliminatedTrue(String name);
    Brand findBrandByNameAndEliminatedFalse(String name);
}
