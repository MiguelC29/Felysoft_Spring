package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Provider;

import java.util.List;

public interface ProviderService {
    public List<Provider> findAll() throws Exception;
    public void create(Provider provider);
    public void update(Provider provider);
    public void delete(Provider provider);
}