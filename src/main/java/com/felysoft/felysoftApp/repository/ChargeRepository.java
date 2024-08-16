package com.felysoft.felysoftApp.repository;

import com.felysoft.felysoftApp.entity.Charge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChargeRepository extends JpaRepository<Charge, Long> {
    List<Charge> findChargeByEliminatedFalse();

    List<Charge> findChargeByEliminatedTrue();

    Charge findChargeByIdChargeAndEliminatedFalse(Long id);

    Charge findChargeByIdChargeAndEliminatedTrue(Long id);

    Charge findChargeByChargeAndEliminatedTrue(String charge);
}
