package com.felysoft.felysoftApp.repositories;

import com.felysoft.felysoftApp.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>{
}