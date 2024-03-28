package com.felysoft.felysoftApp.services.imp;

import com.felysoft.felysoftApp.entities.Charge;
import com.felysoft.felysoftApp.entities.Employee;
import com.felysoft.felysoftApp.repositories.ChargeRepository;
import com.felysoft.felysoftApp.repositories.EmployeeRepository;
import com.felysoft.felysoftApp.services.ChargeService;
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