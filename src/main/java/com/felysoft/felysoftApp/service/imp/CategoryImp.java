package com.felysoft.felysoftApp.service.imp;

import com.felysoft.felysoftApp.entity.Category;
import com.felysoft.felysoftApp.entity.Provider;
import com.felysoft.felysoftApp.repository.CategoryRepository;
import com.felysoft.felysoftApp.repository.ProviderRepository;
import com.felysoft.felysoftApp.service.CategoryService;
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
    public List<Category> findByIdProvider(Long id) {
        return this.categoryRepository.findByProviderId(id);
    }

    @Override
    public Category findCategoryByNameAndEliminated(String name) {
        return this.categoryRepository.findCategoryByNameAndEliminatedTrue(name);
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

    // Método para verificar si la asociación entre la categoría y el proveedor ya existe
    @Override
    public boolean checkAssociationExists(Long categoryId, Long providerId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        Provider provider = providerRepository.findById(providerId).orElse(null);

        // Verificar si el proveedor ya está asociado a la categoría
        return category != null && provider != null && category.getProviders().contains(provider);
    }
}
