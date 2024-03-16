package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Category;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CategoryService {
    public List<Category> findAll() throws Exception;
    public Category findById(Long id);
    public List<Category> findByIdProvider(Long id);

    @Transactional
    public void create(Category category);

    @Transactional
    @Modifying
    public void update(Category category);

    @Transactional
    @Modifying
    public void delete(Category category);

    @Transactional
    public void addProviderToCategory(Long categoryId, Long providerId);
}