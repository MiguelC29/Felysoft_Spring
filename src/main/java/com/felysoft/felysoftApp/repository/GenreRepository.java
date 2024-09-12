package com.felysoft.felysoftApp.repository;

import com.felysoft.felysoftApp.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    @Query("SELECT g.name, a.name FROM Genre g JOIN g.authors a WHERE g.eliminated = false AND a.eliminated = false")
    List<Object[]> findGenreAuthorNames();

    @Query("SELECT g FROM Genre g JOIN g.authors a WHERE a.idAuthor = :authorId AND g.eliminated = false")
    List<Genre> findByAuthorId(@Param("authorId") Long idAuthor);

    List<Genre> findGenresByEliminatedFalse();
    List<Genre> findGenresByEliminatedTrue();

    Genre findGenreByIdGenreAndEliminatedFalse(Long id);
    Genre findGenreByIdGenreAndEliminatedTrue(Long id);
    Genre findGenreByNameAndEliminatedTrue(String name);
    Genre findGenreByNameAndEliminatedFalse(String name);
}
