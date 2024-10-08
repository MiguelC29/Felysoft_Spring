package com.felysoft.felysoftApp.service;

import com.felysoft.felysoftApp.entity.Provider;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProviderService {
    List<Provider> findAll() throws Exception;

    List<Provider> findAllDisabled() throws Exception;

    Provider findById(Long id);

    Provider findByIdDisabled(Long id);

    List<Provider> findByIdCategory(Long id);

    Provider findProviderByNitAndEliminated(String nit);

    Provider findProviderByName(String name);

    @Transactional
    void create(Provider provider);

    @Transactional
    @Modifying
    void update(Provider provider);

    @Transactional
    @Modifying
    void delete(Provider provider);
}