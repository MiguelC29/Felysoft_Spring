package com.felysoft.felysoftApp.repository;

import com.felysoft.felysoftApp.entity.Detail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailRepository extends JpaRepository <Detail, Long> {
    List<Detail> findDetailByEliminatedFalse();

    Detail findDetailByIdDetailAndEliminatedFalse(Long id);
}
