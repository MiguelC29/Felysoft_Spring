package com.felysoft.felysoftApp.repositories;

import com.felysoft.felysoftApp.entities.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findServicesByEliminatedFalse();

    Service findServicesByIdServiceAndEliminatedFalse(Long id);
}