package com.felysoft.felysoftApp.service.imp;

import com.felysoft.felysoftApp.entity.Service;
import com.felysoft.felysoftApp.repository.ServiceRepository;
import com.felysoft.felysoftApp.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceImp implements ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public List<Service> findAll() throws Exception {
        return this.serviceRepository.findServicesByEliminatedFalse();
    }

    @Override
    public Service findById(Long id) {
        return this.serviceRepository.findServicesByIdServiceAndEliminatedFalse(id);
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
        this.serviceRepository.save(service);
    }
}
