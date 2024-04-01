package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Author;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AuthorService {
    public List<Author> findAll() throws Exception;
    public Author findById(Long id);

    public List<Author> findByIdGenre(Long id);
    @Transactional
    public void create(Author author);
    @Transactional
    @Modifying
    public void update(Author author);
    @Transactional
    @Modifying
    public void delete(Author author);
}