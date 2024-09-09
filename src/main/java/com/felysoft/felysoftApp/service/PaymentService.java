package com.felysoft.felysoftApp.service;

import com.felysoft.felysoftApp.entity.Payment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PaymentService {
    List<Payment> findAll() throws Exception;
    List<Payment> findAllDisabled() throws Exception;

    Payment findById(Long id);
    Payment findByIdDisabled(Long id);

    @Transactional
    void create(Payment payment);

    @Transactional
    @Modifying
    void update(Payment payment);

    @Transactional
    @Modifying
    void delete(Payment payment);
}
