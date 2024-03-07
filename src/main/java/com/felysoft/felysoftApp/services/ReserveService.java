package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Reserve;

import java.util.List;

public interface ReserveService {
    public List<Reserve> findAll() throws Exception;
    public Reserve findById(Long id);
    public void create(Reserve reserve);
    public void update(Reserve reserve);
    public void delete(Reserve reserve);
}