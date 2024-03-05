package com.felysoft.felysoftApp.repositories;

import com.felysoft.felysoftApp.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book,Long>{
}
