package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Payment;
import java.util.List;

public interface PaymentService {
    public List<Payment> findAll() throws Exception;
    public Payment findById(Long id);
    public void create(Payment payment);
    public void update(Payment payment);
    public void delete(Payment payment);
}