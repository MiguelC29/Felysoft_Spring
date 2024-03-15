package com.felysoft.felysoftApp.repositories;

import com.felysoft.felysoftApp.entities.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {

    List<Provider> findProvidersByEliminatedFalse();
    Provider findProviderByIdProviderAndEliminatedFalse(Long id);
}