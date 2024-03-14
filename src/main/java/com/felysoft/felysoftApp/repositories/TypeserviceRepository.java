package com.felysoft.felysoftApp.repositories;

import com.felysoft.felysoftApp.entities.TypeService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeserviceRepository extends JpaRepository<TypeService, Long> {
    /*@Query("SELECT t FROM TypeService t WHERE t.eliminated = false")
    List<TypeService> findAll();*/

    List<TypeService> findTypeservicesByByEliminatedFalse();
}