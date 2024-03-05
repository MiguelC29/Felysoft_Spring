package com.felysoft.felysoftApp.repositories;

import com.felysoft.felysoftApp.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository <Sale, Long> {

}
