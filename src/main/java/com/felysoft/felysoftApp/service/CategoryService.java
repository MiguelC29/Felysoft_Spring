package com.felysoft.felysoftApp.service;

import com.felysoft.felysoftApp.entity.Category;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CategoryService {
    List<Category> findAll() throws Exception;

    List<Category> findAllDisabled() throws Exception;

    Category findById(Long id);

    Category findByIdDisabled(Long id);

    List<Category> findByIdProvider(Long id);

    Category findCategoryByNameAndEliminated(String name);

    Category findCategoryByName(String name);

    List<Object[]> findCategoryProviderNames();

    @Transactional
    void create(Category category);

    @Transactional
    @Modifying
    void update(Category category);

    @Transactional
    @Modifying
    void delete(Category category);

    @Transactional
    void addProviderToCategory(Long categoryId, Long providerId);

    boolean checkAssociationExists(Long categoryId, Long providerId);
}
