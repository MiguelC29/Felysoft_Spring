package com.felysoft.felysoftApp.services.imp;

import com.felysoft.felysoftApp.entities.Category;
import com.felysoft.felysoftApp.entities.Provider;
import com.felysoft.felysoftApp.repositories.CategoryRepository;
import com.felysoft.felysoftApp.repositories.ProviderRepository;
import com.felysoft.felysoftApp.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryImp implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Override
    public List<Category> findAll() throws Exception {
        return this.categoryRepository.findCategoriesByEliminatedFalse();
    }

    @Override
    public Category findById(Long id) {
        return this.categoryRepository.findCategoryByIdCategoryAndEliminatedFalse(id);
    }

    @Override
    public void create(Category category) {
        this.categoryRepository.save(category);
    }

    @Override
    public void update(Category category) {
        this.categoryRepository.save(category);
    }

    @Override
    public void delete(Category category) {
        this.categoryRepository.save(category);
    }

    @Override
    public void addProviderToCategory(Long categoryId, Long providerId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        Provider provider = providerRepository.findById(providerId).orElse(null);

        category.getProviders().add(provider);
        this.categoryRepository.save(category);
    }
}