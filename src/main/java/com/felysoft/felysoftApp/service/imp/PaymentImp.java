package com.felysoft.felysoftApp.service.imp;

import com.felysoft.felysoftApp.entity.Payment;
import com.felysoft.felysoftApp.repository.PaymentRepository;
import com.felysoft.felysoftApp.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentImp implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public List<Payment> findAll() throws Exception {
        return  this.paymentRepository.findPaymentByEliminatedFalse();
    }

    @Override
    public Payment findById(Long id) {
        return this.paymentRepository.findPaymentByIdPaymentAndEliminatedFalse(id);
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
        this.paymentRepository.save(payment);
    }
}
