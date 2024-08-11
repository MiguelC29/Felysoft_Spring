package com.felysoft.felysoftApp.service.imp;

import com.felysoft.felysoftApp.entity.Book;
import com.felysoft.felysoftApp.repository.BookRepository;
import com.felysoft.felysoftApp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookImp implements BookService{

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> findAll() throws Exception {
        return this.bookRepository.findBooksByEliminatedFalse();
    }

    @Override
    public List<Book> findAllDisabled() {
        return this.bookRepository.findBooksByEliminatedTrue();
    }

    @Override
    public Book findById(Long id) {
        return this.bookRepository.findBookByIdBookAndEliminatedFalse(id);
    }

    @Override
    public Book findByIdDisabled(Long id) {
        return this.bookRepository.findBookByIdBookAndEliminatedTrue(id);
    }

    @Override
    public Book findBookByTitleAndEliminated(String title) {
        return this.bookRepository.findBookByTitleAndEliminatedTrue(title);
    }

    @Override
    public void create(Book book) {
        this.bookRepository.save(book);
    }

    @Override
    public void update(Book book) {
        this.bookRepository.save(book);
    }

    @Override
    public void delete(Book book) {
        this.bookRepository.save(book);
    }
}
