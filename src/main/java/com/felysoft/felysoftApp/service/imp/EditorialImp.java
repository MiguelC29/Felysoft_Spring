package com.felysoft.felysoftApp.service.imp;

import com.felysoft.felysoftApp.entity.Editorial;
import com.felysoft.felysoftApp.repository.EditorialRepository;
import com.felysoft.felysoftApp.service.EditorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EditorialImp implements EditorialService {
    @Autowired
    private EditorialRepository editorialRepository;
    @Override
    public List<Editorial> findAll() throws Exception {
        return this.editorialRepository.findEditorialsByEliminatedFalse();
    }

    @Override
    public List<Editorial> findAllDisabled() throws Exception {
        return this.editorialRepository.findEditorialsByEliminatedTrue();
    }

    @Override
    public Editorial findById(Long id) {
        return this.editorialRepository.findEditorialByIdEditorialAndEliminatedFalse(id);
    }

    @Override
    public Editorial findByIdDisabled(Long id) {
        return this.editorialRepository.findEditorialByIdEditorialAndEliminatedTrue(id);
    }

    @Override
    public Editorial findGenreByNameAndEliminated(String name) {
        return this.editorialRepository.findEditorialByNameAndEliminatedTrue(name);
    }

    @Override
    public void create(Editorial editorial) {
        this.editorialRepository.save(editorial);
    }

    @Override
    public void update(Editorial editorial) {
        this.editorialRepository.save(editorial);
    }

    @Override
    public void delete(Editorial editorial) {
        this.editorialRepository.save(editorial);
    }

}
