package com.felysoft.felysoftApp.repositories;

import com.felysoft.felysoftApp.entities.Book;
import com.felysoft.felysoftApp.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{
    List<Book> findBooksByEliminatedFalse();

    Book findBookByIdBookAndEliminatedFalse(Long id);
}