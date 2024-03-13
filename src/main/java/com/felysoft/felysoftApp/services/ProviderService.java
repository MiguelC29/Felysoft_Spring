package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Provider;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProviderService {
    public List<Provider> findAll() throws Exception;
    public Provider findById(Long id);

    @Transactional
    public void create(Provider provider);

    @Transactional
    @Modifying
    public void update(Provider provider);

    @Transactional
    @Modifying
    public void delete(Provider provider);
}