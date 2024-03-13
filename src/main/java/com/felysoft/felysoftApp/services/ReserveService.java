package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Reserve;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReserveService {
    public List<Reserve> findAll() throws Exception;
    public Reserve findById(Long id);
    @Transactional
    public void create(Reserve reserve);
    @Transactional
    @Modifying
    public void update(Reserve reserve);
    @Transactional
    @Modifying
    public void delete(Reserve reserve);
}