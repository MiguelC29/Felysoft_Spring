package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Detail;
import java.util.List;

public interface DetailService {
    public List<Detail> findAll() throws Exception;
    public Detail findById(Long id);
    public void create(Detail detail);
    public void update(Detail detail);
    public void delete(Detail detail);
}