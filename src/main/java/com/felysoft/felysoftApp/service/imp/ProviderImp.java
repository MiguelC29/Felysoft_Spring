package com.felysoft.felysoftApp.service.imp;

import com.felysoft.felysoftApp.entity.Provider;
import com.felysoft.felysoftApp.repository.ProviderRepository;
import com.felysoft.felysoftApp.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProviderImp implements ProviderService {

    @Autowired
    private ProviderRepository providerRepository;

    @Override
    public List<Provider> findAll() throws Exception {
        return this.providerRepository.findProvidersByEliminatedFalse();
    }

    @Override
    public List<Provider> findAllDisabled() throws Exception {
        return this.providerRepository.findProvidersByEliminatedTrue();
    }

    @Override
    public Provider findById(Long id) {
        return this.providerRepository.findProviderByIdProviderAndEliminatedFalse(id);
    }

    @Override
    public Provider findByIdDisabled(Long id) {
        return this.providerRepository.findProviderByIdProviderAndEliminatedTrue(id);
    }

    @Override
    public List<Provider> findByIdCategory(Long id) {
        return this.providerRepository.findByCategoryId(id);
    }

    @Override
    public Provider findProviderByNitAndEliminated(String nit) {
        return this.providerRepository.findProviderByNitAndEliminatedTrue(nit);
    }

    @Override
    public Provider findProviderByName(String name) {
        return this.providerRepository.findProviderByNameAndEliminatedFalse(name);
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
        this.providerRepository.save(provider);
    }
}