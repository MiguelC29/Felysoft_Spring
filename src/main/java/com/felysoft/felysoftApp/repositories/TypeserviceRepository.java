package com.felysoft.felysoftApp.repositories;

import com.felysoft.felysoftApp.entities.TypeService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeserviceRepository extends JpaRepository<TypeService, Long> {

    //CONSULTA CON INVERSION DE CONTROL
    List<TypeService> findTypeServicesByEliminatedFalse();

    TypeService findTypeServiceByIdTypeServiceAndEliminatedFalse(Long id);
}