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
    public List<Genre> findAllDisabled() throws Exception {
        return this.genreRepository.findGenresByEliminatedTrue();
    }

    @Override
    public Genre findById(Long id) {
        return this.genreRepository.findGenreByIdGenreAndEliminatedFalse(id);
    }

    @Override
    public Genre findByIdDisabled(Long id) {
        return this.genreRepository.findGenreByIdGenreAndEliminatedTrue(id);
    }

    @Override
    public List<Genre> findByIdAuthor(Long id) {
        return this.genreRepository.findByAuthorId(id);
    }

    @Override
    public Genre findGenreByNameAndEliminated(String name){
        return this.genreRepository.findGenreByNameAndEliminatedTrue(name);
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

    //Para verificación de si la asociación entre genero y autor existe
    @Override
    public boolean checkAssociationExists(Long genreId, Long authorId) {
        Genre genre = genreRepository.findById(genreId).orElse(null);
        Author author = authorRepository.findById(authorId).orElse(null);

        //Esto Verifica si el autor ya está asociado al género
        return genre != null && author != null && genre.getAuthors().contains(author);
    }
}
