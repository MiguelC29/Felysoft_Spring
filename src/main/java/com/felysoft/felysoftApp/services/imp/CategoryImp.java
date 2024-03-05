package com.felysoft.felysoftApp.services.imp;

import com.felysoft.felysoftApp.entities.Category;
import com.felysoft.felysoftApp.repositories.CategoryRepository;
import com.felysoft.felysoftApp.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryImp implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() throws Exception {
        return this.categoryRepository.findAll();
    }

    @Override
    public Category findById(Long id) {
        return this.categoryRepository.findById(id).orElse(null);
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
        this.categoryRepository.delete(category);
    }
}