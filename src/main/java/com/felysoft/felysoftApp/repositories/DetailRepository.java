package com.felysoft.felysoftApp.repositories;

import com.felysoft.felysoftApp.entities.Detail;
import com.felysoft.felysoftApp.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailRepository extends JpaRepository <Detail, Long> {

    List<Detail> findDetailByEliminatedFalse();

    Detail findDetailByIdDetailAndEliminatedFalse(Long id);
}