package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Category;

import java.util.List;

public interface CategoryService {
    public List<Category> findAll() throws Exception;
    public void create(Category category);
    public void update(Category category);
    public void delete(Category category);
}