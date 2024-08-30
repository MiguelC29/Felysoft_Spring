package com.felysoft.felysoftApp.service;

import com.felysoft.felysoftApp.entity.Brand;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BrandService {

    List<Brand> findAll() throws Exception;

    List<Brand> findAllDisabled() throws Exception;

    Brand findById(Long id) throws Exception;

    Brand findByIdDisabled(Long id) throws Exception;

    Brand findBrandByNameAndEliminated(String name);

    Brand findCategoryByName(String name);

    @Transactional
    void create(Brand brand) throws Exception;

    @Transactional
    @Modifying
    void update(Brand brand) throws Exception;

    @Transactional
    @Modifying
    void delete(Brand brand) throws Exception;
}
