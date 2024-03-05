package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Book;

import java.util.List;

public interface BookService {
    public List<Book> findAll() throws Exception;
    public Book findById(Long id);
    public void create(Book book);
    public void update(Book book);
    public void delete(Book book);
}
