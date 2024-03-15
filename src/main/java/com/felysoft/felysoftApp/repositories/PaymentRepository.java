package com.felysoft.felysoftApp.repositories;

import com.felysoft.felysoftApp.entities.Payment;
import com.felysoft.felysoftApp.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findPaymentByEliminatedFalse();

    Payment findPaymentByIdPaymentAndEliminatedFalse(Long id);
}