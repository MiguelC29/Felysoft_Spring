package com.felysoft.felysoftApp.repository;

import com.felysoft.felysoftApp.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{
    List<Book> findBooksByEliminatedFalse();
    List<Book> findBooksByEliminatedTrue();

    @Query("SELECT b FROM Book b JOIN b.editorial e WHERE e.idEditorial = :editorialId AND b.eliminated = false AND NOT EXISTS (SELECT i FROM Inventory i WHERE i.book = b)")
    List<Book> findByEditorialId(@Param("editorialId") Long idEditorial);

    @Query("SELECT b FROM Book b WHERE b.eliminated = false AND EXISTS (SELECT i FROM Inventory i WHERE i.book = b)")
    List<Book> findByInventoryFalse();

    Book findBookByIdBookAndEliminatedFalse(Long id);
    Book findBookByIdBookAndEliminatedTrue(Long id);
    Book findBookByTitleAndEliminatedTrue(String title);
    Book findBookByTitle(String title);
}
