package com.felysoft.felysoftApp.services.imp;

import com.felysoft.felysoftApp.entities.Genre;
import com.felysoft.felysoftApp.repositories.GenreRepository;
import com.felysoft.felysoftApp.services.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreImp implements GenreService {

    @Autowired
    private GenreRepository genreRepository;

    @Override
    public List<Genre> findAll() throws Exception {
        return this.genreRepository.findAll();
    }

    @Override
    public Genre findById(Long id) {
        return this.genreRepository.findById(id).orElse(null);
    }

    @Override
    public void create(Genre genre) {
        this.genreRepository.save(genre);
    }

    @Override
    public void update(Genre genre) {
        this.genreRepository.save(genre);
    }

    @Override
    public void delete(Genre genre) {
        this.genreRepository.delete(genre);
    }
}