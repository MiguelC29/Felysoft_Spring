package com.felysoft.felysoftApp.services.imp;

import com.felysoft.felysoftApp.entities.Payment;
import com.felysoft.felysoftApp.repositories.PaymentRepository;
import com.felysoft.felysoftApp.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentImp implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public List<Payment> findAll() throws Exception {
        return  this.paymentRepository.findAll();
    }

    @Override
    public Payment findById(Long id) {
        return this.paymentRepository.findById(id).orElse(null);
    }

    @Override
    public void create(Payment payment) {
        this.paymentRepository.save(payment);
}

    @Override
    public void update(Payment payment) {
        this.paymentRepository.save(payment);
    }

    @Override
    public void delete(Payment payment) {
        this.paymentRepository.delete(payment);
    }
}