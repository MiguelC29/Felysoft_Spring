package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Book;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BookService {
    public List<Book> findAll() throws Exception;
    public Book findById(Long id);
    @Transactional
    public void create(Book book);
    @Transactional
    @Modifying
    public void update(Book book);
    @Transactional
    @Modifying
    public void delete(Book book);
}