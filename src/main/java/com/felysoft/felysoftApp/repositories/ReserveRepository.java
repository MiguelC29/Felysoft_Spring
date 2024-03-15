package com.felysoft.felysoftApp.repositories;

import com.felysoft.felysoftApp.entities.Reserve;
import com.felysoft.felysoftApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReserveRepository extends JpaRepository<Reserve, Long>{
    List<Reserve> findReservesByEliminatedFalse();

    Reserve findReservesByIdReserveAndEliminatedFalse(Long id);
}