package com.felysoft.felysoftApp.service;

import com.felysoft.felysoftApp.entity.Payment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PaymentService {
    public List<Payment> findAll() throws Exception;

    public Payment findById(Long id);

    @Transactional
    public void create(Payment payment);

    @Transactional
    @Modifying
    public void update(Payment payment);

    @Transactional
    @Modifying
    public void delete(Payment payment);
}
