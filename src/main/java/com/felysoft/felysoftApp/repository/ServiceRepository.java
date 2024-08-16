package com.felysoft.felysoftApp.repository;

import com.felysoft.felysoftApp.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findServicesByEliminatedFalse();

    List<Service> findServicesByEliminatedTrue();

    Service findServicesByIdServiceAndEliminatedFalse(Long id);

    Service findServicesByIdServiceAndEliminatedTrue(Long id);
}
