package com.felysoft.felysoftApp.repository;

import com.felysoft.felysoftApp.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findServicesByEliminatedFalse();

    Service findServicesByIdServiceAndEliminatedFalse(Long id);
}
