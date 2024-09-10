package com.felysoft.felysoftApp.service;

import com.felysoft.felysoftApp.entity.Editorial;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EditorialService {

    List<Editorial> findAll() throws Exception;

    List<Editorial> findAllDisabled() throws Exception;

    Editorial findById(Long id);

    Editorial findByIdDisabled(Long id);

    Editorial findGenreByNameAndEliminated(String name);

    @Transactional
    void create(Editorial editorial);

    @Transactional
    @Modifying
    void update(Editorial editorial);

    @Transactional
    @Modifying
        void delete(Editorial editorial);

}
