package com.felysoft.felysoftApp.repository;

import com.felysoft.felysoftApp.entity.TypeService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeserviceRepository extends JpaRepository<TypeService, Long> {
    //CONSULTA CON INVERSION DE CONTROL
    List<TypeService> findTypeServicesByEliminatedFalse();

    List<TypeService> findTypeServicesByEliminatedTrue();

    TypeService findTypeServiceByIdTypeServiceAndEliminatedFalse(Long id);

    TypeService findTypeServiceByIdTypeServiceAndEliminatedTrue(Long id);

    TypeService findTypeServiceByNameAndEliminatedTrue(String name);
}
