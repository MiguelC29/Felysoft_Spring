package com.felysoft.felysoftApp.services.imp;

import com.felysoft.felysoftApp.entities.Detail;
import com.felysoft.felysoftApp.repositories.DetailRepository;
import com.felysoft.felysoftApp.services.DetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetailImp implements DetailService {

    @Autowired
    private DetailRepository detailRepository;

    @Override
    public List<Detail> findAll() throws Exception {
        return this.detailRepository.findAll();
    }

    @Override
    public Detail findById(Long id) {
        return this.detailRepository.findById(id).orElse(null);
    }

    @Override
    public void create(Detail detail) {
        this.detailRepository.save(detail);
    }

    @Override
    public void update(Detail detail) {
        this.detailRepository.save(detail);
    }

    @Override
    public void delete(Detail detail) {
        this.detailRepository.delete(detail);
    }
}