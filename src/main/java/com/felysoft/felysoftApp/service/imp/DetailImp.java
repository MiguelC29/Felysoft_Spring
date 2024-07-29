package com.felysoft.felysoftApp.service.imp;

import com.felysoft.felysoftApp.entity.Detail;
import com.felysoft.felysoftApp.repository.DetailRepository;
import com.felysoft.felysoftApp.service.DetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetailImp implements DetailService {

    @Autowired
    private DetailRepository detailRepository;

    @Override
    public List<Detail> findAll() throws Exception {
        return this.detailRepository.findDetailByEliminatedFalse();
    }

    @Override
    public Detail findById(Long id) {
        return this.detailRepository.findDetailByIdDetailAndEliminatedFalse(id);
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
        this.detailRepository.save(detail);
    }
}
