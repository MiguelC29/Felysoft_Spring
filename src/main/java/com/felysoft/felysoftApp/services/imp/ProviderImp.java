package com.felysoft.felysoftApp.services.imp;

import com.felysoft.felysoftApp.entities.Provider;
import com.felysoft.felysoftApp.repositories.ProviderRepository;
import com.felysoft.felysoftApp.services.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProviderImp implements ProviderService {

    @Autowired
    private ProviderRepository providerRepository;

    @Override
    public List<Provider> findAll() throws Exception {
        return this.providerRepository.findAll();
    }

    @Override
    public Provider findById(Long id) {
        return this.providerRepository.findById(id).orElse(null);
        // Sin el .orElse(null); da error | RECOMENDACION IDE AGREGAR Optional<Provider>
    }

    @Override
    public void create(Provider provider) {
        this.providerRepository.save(provider);
    }

    @Override
    public void update(Provider provider) {
        this.providerRepository.save(provider);
    }

    @Override
    public void delete(Provider provider) {
        this.providerRepository.delete(provider);
    }
}