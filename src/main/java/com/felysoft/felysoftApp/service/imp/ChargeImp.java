package com.felysoft.felysoftApp.service.imp;

import com.felysoft.felysoftApp.entity.Charge;
import com.felysoft.felysoftApp.repository.ChargeRepository;
import com.felysoft.felysoftApp.service.ChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChargeImp implements ChargeService {

    @Autowired
    private ChargeRepository chargeRepository;

    @Override
    public List<Charge> findAll() throws Exception {
        return this.chargeRepository.findChargeByEliminatedFalse();
    }

    @Override
    public Charge findById(Long id) {
        return this.chargeRepository.findChargeByIdChargeAndEliminatedFalse(id);
    }

    @Override
    public void create(Charge charge) {
        this.chargeRepository.save(charge);
    }

    @Override
    public void update(Charge charge) {
        this.chargeRepository.save(charge);
    }

    @Override
    public void delete(Charge charge) {
        this.chargeRepository.save(charge);
    }
}
