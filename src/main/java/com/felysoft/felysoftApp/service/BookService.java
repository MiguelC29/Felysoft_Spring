package com.felysoft.felysoftApp.service;

import com.felysoft.felysoftApp.entity.Book;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BookService {
    List<Book> findAll() throws Exception;

    Book findById(Long id);

    @Transactional
    void create(Book book);

    @Transactional
    @Modifying
    void update(Book book);

    @Transactional
    @Modifying
    void delete(Book book);
}
