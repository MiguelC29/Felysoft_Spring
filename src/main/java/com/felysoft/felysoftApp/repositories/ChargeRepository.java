package com.felysoft.felysoftApp.repositories;

import com.felysoft.felysoftApp.entities.Charge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargeRepository extends JpaRepository<Charge, Long> {
}