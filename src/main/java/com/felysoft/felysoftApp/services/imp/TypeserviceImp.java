package com.felysoft.felysoftApp.services.imp;

import com.felysoft.felysoftApp.entities.TypeService;
import com.felysoft.felysoftApp.repositories.TypeserviceRepository;
import com.felysoft.felysoftApp.services.TypeserviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeserviceImp implements TypeserviceService {

    @Autowired
    private TypeserviceRepository typeserviceRepository;

    @Override
    public List<TypeService> findAll() throws Exception {
        return this.typeserviceRepository.findTypeservicesByByEliminatedFalse();
    }

    @Override
    public TypeService findById(Long id) {
        return this.typeserviceRepository.findById(id).orElse(null);
    }

    @Override
    public void create(TypeService typeService) {
        this.typeserviceRepository.save(typeService);
    }

    @Override
    public void update(TypeService typeService) {
        this.typeserviceRepository.save(typeService);
    }

    @Override
    public void delete(TypeService typeService) {
        this.typeserviceRepository.save(typeService);
    }
}