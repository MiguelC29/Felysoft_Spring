package com.felysoft.felysoftApp.service.imp;

import com.felysoft.felysoftApp.entity.Reserve;
import com.felysoft.felysoftApp.entity.User;
import com.felysoft.felysoftApp.repository.ReserveRepository;
import com.felysoft.felysoftApp.service.ReserveService;
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
    public List<Reserve> findAllDisabled() throws Exception {
        return this.reserveRepository.findReservesByEliminatedTrue();
    }

    @Override
    public List<Reserve> findReservesByUser(User user) throws Exception {
        return this.reserveRepository.findReservesByEliminatedFalseAndUser(user);
    }

    @Override
    public List<Reserve> findReservesByUserActive(User user, Reserve.State state) throws Exception {
        return this.reserveRepository.findReservesByEliminatedFalseAndUserAndState(user, state);
    }

    @Override
    public Reserve findById(Long id) {
        return this.reserveRepository.findReservesByIdReserveAndEliminatedFalse(id);
    }

    @Override
    public Reserve findByIdDisabled(Long id) {
        return this.reserveRepository.findReservesByIdReserveAndEliminatedTrue(id);
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

    @Override
    public void cancel(Reserve reserve) {
        this.reserveRepository.save(reserve);
    }
}
