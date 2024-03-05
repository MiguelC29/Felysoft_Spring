package com.felysoft.felysoftApp.repositories;

import com.felysoft.felysoftApp.entities.Reserve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReserveRepository extends JpaRepository<Reserve,Long>{
}
