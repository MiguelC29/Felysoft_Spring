package com.felysoft.felysoftApp.service;

import com.felysoft.felysoftApp.entity.Author;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AuthorService {
    List<Author> findAll() throws Exception;

    Author findById(Long id);

    List<Author> findByIdGenre(Long id);

    @Transactional
    void create(Author author);

    @Transactional
    @Modifying
    void update(Author author);

    @Transactional
    @Modifying
    void delete(Author author);
}
