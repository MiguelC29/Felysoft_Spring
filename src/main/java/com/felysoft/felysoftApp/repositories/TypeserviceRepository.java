package com.felysoft.felysoftApp.repositories;

import com.felysoft.felysoftApp.entities.TypeService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeserviceRepository extends JpaRepository<TypeService, Long> {
}