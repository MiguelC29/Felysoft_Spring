package com.felysoft.felysoftApp.service;

import com.felysoft.felysoftApp.entity.Genre;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GenreService {
    List<Genre> findAll() throws Exception;

    List<Genre> findAllDisabled() throws Exception;

    Genre findById(Long id);

    Genre findByIdDisabled(Long id);

    List<Genre> findByIdAuthor(Long id);

    Genre findGenreByNameAndEliminated(String name);

    Genre findGenreByName(String name);

    List<Object[]> findGenreAuthorNames();

    @Transactional
    void create(Genre genre);

    @Transactional
    @Modifying
    void update(Genre genre);

    @Transactional
    @Modifying
    void delete(Genre genre);

    @Transactional
    void addAuthorToGenre( Long genreId, Long authorId );

    boolean checkAssociationExists(Long genreId, Long authorId);
}
