package com.felysoft.felysoftApp.service.imp;

import com.felysoft.felysoftApp.entity.Author;
import com.felysoft.felysoftApp.repository.AuthorRepository;
import com.felysoft.felysoftApp.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorImp implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public List<Author> findAll() throws Exception {
        return this.authorRepository.findAuthorsByEliminatedFalse();
    }

    @Override
    public List<Author> findAllDisabled() {
        return this.authorRepository.findAuthorsByEliminatedTrue();
    }

    @Override
    public Author findById(Long id) {
        return this.authorRepository.findAuthorByIdAuthorAndEliminatedFalse(id);
    }

    @Override
    public Author findByIdDisabled(Long id) {
        return this.authorRepository.findAuthorByIdAuthorAndEliminatedTrue(id);
    }

    @Override
    public List<Author> findByIdGenre(Long id) {
        return this.authorRepository.findByGenreId(id);
    }

    @Override
    public Author findAuthorByNameAndEliminated(String name){
        return this.authorRepository.findAuthorByNameAndEliminatedTrue(name);
    }

    @Override
    public Author findAuthorByName(String name) {
        return this.authorRepository.findAuthorByNameAndEliminatedFalse(name);
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
        this.authorRepository.save(author);
    }
}
