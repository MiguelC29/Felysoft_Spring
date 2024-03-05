package com.felysoft.felysoftApp.services.imp;

import com.felysoft.felysoftApp.entities.Service;
import com.felysoft.felysoftApp.repositories.ServiceRepository;
import com.felysoft.felysoftApp.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceImp implements ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public List<Service> findAll() throws Exception {
        return this.serviceRepository.findAll();
    }

    @Override
    public Service findById(Long id) {
        return this.serviceRepository.findById(id).orElse(null);
    }

    @Override
    public void create(Service service) {
        this.serviceRepository.save(service);
    }

    @Override
    public void update(Service service) {
        this.serviceRepository.save(service);
    }

    @Override
    public void delete(Service service) {
        this.serviceRepository.delete(service);
    }
}
