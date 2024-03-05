package com.felysoft.felysoftApp.services.imp;

import com.felysoft.felysoftApp.entities.Author;
import com.felysoft.felysoftApp.repositories.AuthorRepository;
import com.felysoft.felysoftApp.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorImp implements AuthorService {
    @Autowired
    private AuthorRepository authorRepository;
    @Override
    public List<Author> findAll() throws Exception {
        return this.authorRepository.findAll();
    }

    @Override
    public Author findById(Long id) {
        return this.authorRepository.findById(id).orElse(null);
    }

    @Override
    public void create(Author author) {
        this.authorRepository.save(author);
    }

    @Override
    public void update(Author author) {
        this.authorRepository.save(author);
    }

    @Override
    public void delete(Author author) {
        this.authorRepository.delete(author);
    }
}
