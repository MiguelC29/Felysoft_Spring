package com.felysoft.felysoftApp.repository;

import com.felysoft.felysoftApp.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findPaymentByEliminatedFalse();

    Payment findPaymentByIdPaymentAndEliminatedFalse(Long id);
}
