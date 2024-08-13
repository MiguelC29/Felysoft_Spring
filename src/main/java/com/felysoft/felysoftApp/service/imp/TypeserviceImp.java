package com.felysoft.felysoftApp.service.imp;

import com.felysoft.felysoftApp.entity.TypeService;
import com.felysoft.felysoftApp.repository.TypeserviceRepository;
import com.felysoft.felysoftApp.service.TypeserviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeserviceImp implements TypeserviceService {

    @Autowired
    private TypeserviceRepository typeserviceRepository;

    @Override
    public List<TypeService> findAll() throws Exception {
        return this.typeserviceRepository.findTypeServicesByEliminatedFalse();
    }

    @Override
    public List<TypeService> findAllDisabled() throws Exception {
        return this.typeserviceRepository.findTypeServicesByEliminatedTrue();
    }

    @Override
    public TypeService findById(Long id) {
        return this.typeserviceRepository.findTypeServiceByIdTypeServiceAndEliminatedFalse(id);
    }

    @Override
    public TypeService findByIdDisabled(Long id) {
        return this.typeserviceRepository.findTypeServiceByIdTypeServiceAndEliminatedTrue(id);
    }

    @Override
    public TypeService findByNameAndEliminated(String name) {
        return this.typeserviceRepository.findTypeServiceByNameAndEliminatedTrue(name);
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
