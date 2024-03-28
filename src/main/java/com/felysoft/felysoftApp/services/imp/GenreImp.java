package com.felysoft.felysoftApp.services.imp;

import com.felysoft.felysoftApp.entities.Author;
import com.felysoft.felysoftApp.entities.Genre;
import com.felysoft.felysoftApp.repositories.AuthorRepository;
import com.felysoft.felysoftApp.repositories.GenreRepository;
import com.felysoft.felysoftApp.services.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreImp implements GenreService {

    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public List<Genre> findAll() throws Exception {
        return this.genreRepository.findGenresByEliminatedFalse();
    }

    @Override
    public Genre findById(Long id) {
        return this.genreRepository.findGenreByIdGenreAndEliminatedFalse(id);
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
        this.genreRepository.save(genre);
    }

    @Override
    public void addAuthorToGenre( Long genreId, Long authorId) {
        Genre genre = this.genreRepository.findById(genreId).orElse(null);
        Author author = this.authorRepository.findById(authorId).orElse(null);

        genre.getAuthors().add(author);
        this.genreRepository.save(genre);
    }
}