package com.felysoft.felysoftApp.repository;

import com.felysoft.felysoftApp.entity.Reserve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReserveRepository extends JpaRepository<Reserve, Long>{
    List<Reserve> findReservesByEliminatedFalse();

    Reserve findReservesByIdReserveAndEliminatedFalse(Long id);
}
