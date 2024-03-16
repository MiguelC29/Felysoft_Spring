package com.felysoft.felysoftApp.repositories;

import com.felysoft.felysoftApp.entities.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {

    List<Provider> findProvidersByEliminatedFalse();
    Provider findProviderByIdProviderAndEliminatedFalse(Long id);

    @Query("SELECT p FROM Provider p JOIN p.categories c WHERE c.idCategory = :categoryId")
    List<Provider> findByCategoryId(@Param("categoryId") Long idCategory);
}