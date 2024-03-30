package com.felysoft.felysoftApp.services.imp;

import com.felysoft.felysoftApp.entities.Reserve;
import com.felysoft.felysoftApp.repositories.ReserveRepository;
import com.felysoft.felysoftApp.services.ReserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReserveImp implements ReserveService {

    @Autowired
    private ReserveRepository reserveRepository;

    @Override
    public List<Reserve> findAll() throws Exception {
        return this.reserveRepository.findReservesByEliminatedFalse();
    }

    @Override
    public Reserve findById(Long id) {
        return this.reserveRepository.findReservesByIdReserveAndEliminatedFalse(id);
    }

    @Override
    public void create(Reserve reserve) {
         this.reserveRepository.save(reserve);
    }

    @Override
    public void update(Reserve reserve) {
        this.reserveRepository.save(reserve);
    }

    @Override
    public void delete(Reserve reserve) {
        this.reserveRepository.save(reserve);
    }
}