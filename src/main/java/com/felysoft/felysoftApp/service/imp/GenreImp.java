package com.felysoft.felysoftApp.service.imp;

import com.felysoft.felysoftApp.entity.Author;
import com.felysoft.felysoftApp.entity.Genre;
import com.felysoft.felysoftApp.repository.AuthorRepository;
import com.felysoft.felysoftApp.repository.GenreRepository;
import com.felysoft.felysoftApp.service.GenreService;
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
    public List<Genre> findByIdAuthor(Long id) {
        return this.genreRepository.findByAuthorId(id);
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
