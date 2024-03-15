package com.felysoft.felysoftApp.repositories;

import com.felysoft.felysoftApp.entities.Author;
import com.felysoft.felysoftApp.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>{
    List<Author> findAuthorsByEliminatedFalse();

    Author findAuthorByIdAuthorAndEliminatedFalse(Long id);
}