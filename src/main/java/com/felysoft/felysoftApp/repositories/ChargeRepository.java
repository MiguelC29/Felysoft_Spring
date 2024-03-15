package com.felysoft.felysoftApp.repositories;

import com.felysoft.felysoftApp.entities.Charge;
import com.felysoft.felysoftApp.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChargeRepository extends JpaRepository<Charge, Long> {

    List<Charge> findChargeByEliminatedFalse();

    Charge findChargeByIdChargeAndEliminatedFalse(Long id);
}