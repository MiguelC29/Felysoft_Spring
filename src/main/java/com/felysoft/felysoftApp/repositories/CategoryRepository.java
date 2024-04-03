package com.felysoft.felysoftApp.repositories;

import com.felysoft.felysoftApp.entities.Category;
import com.felysoft.felysoftApp.entities.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // CONSULTA CON JPQL
    /*@Query("SELECT c FROM Category c WHERE c.eliminated = false")
    List<Category> findAll();*/
    @Query("SELECT c FROM Category c JOIN c.providers p WHERE p.idProvider = :providerId AND c.eliminated = false")
    List<Category> findByProviderId(@Param("providerId") Long idProvider);

    // CONSULTA CON INVERSIÓN DE CONTROL
    List<Category> findCategoriesByEliminatedFalse();
    Category findCategoryByIdCategoryAndEliminatedFalse(Long id);

    Category findCategoryByNameAndEliminatedTrue(String name);
}