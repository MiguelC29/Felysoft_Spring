package com.felysoft.felysoftApp.repositories;

import com.felysoft.felysoftApp.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>{
    List<Author> findAuthorsByEliminatedFalse();

    Author findAuthorByIdAuthorAndEliminatedFalse(Long id);

    @Query("SELECT a FROM Author a JOIN a.genres g WHERE g.idGenre = :genreId AND a.eliminated = false")
    List<Author> findByGenreId(@Param("genreId") Long idGenre);
}