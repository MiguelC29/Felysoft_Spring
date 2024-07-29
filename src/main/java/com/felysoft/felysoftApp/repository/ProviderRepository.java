package com.felysoft.felysoftApp.repository;

import com.felysoft.felysoftApp.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {
    List<Provider> findProvidersByEliminatedFalse();

    Provider findProviderByIdProviderAndEliminatedFalse(Long id);

    @Query("SELECT p FROM Provider p JOIN p.categories c WHERE c.idCategory = :categoryId AND p.eliminated = false")
    List<Provider> findByCategoryId(@Param("categoryId") Long idCategory);
}
