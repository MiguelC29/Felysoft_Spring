package com.felysoft.felysoftApp.repositories;

import com.felysoft.felysoftApp.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // CONSULTA CON JPQL
    /*@Query("SELECT c FROM Category c WHERE c.eliminated = false")
    List<Category> findAll();*/

    // CONSULTA CON INVERSIÓN DE CONTROL
    List<Category> findCategoriesByEliminatedFalse();
    Category findCategoryByIdCategoryAndEliminatedFalse(Long id);

}