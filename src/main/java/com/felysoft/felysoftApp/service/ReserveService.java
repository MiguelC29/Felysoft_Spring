package com.felysoft.felysoftApp.service;

import com.felysoft.felysoftApp.entity.Reserve;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReserveService {
    List<Reserve> findAll() throws Exception;

    Reserve findById(Long id);

    @Transactional
    void create(Reserve reserve);

    @Transactional
    @Modifying
    void update(Reserve reserve);

    @Transactional
    @Modifying
    void delete(Reserve reserve);
}
