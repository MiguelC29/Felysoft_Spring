package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Author;

import java.util.List;

public interface AuthorService {
    public List<Author> findAll() throws Exception;
    public Author findById(Long id);
    public void create(Author author);
    public void update(Author author);
    public void delete(Author author);
}
