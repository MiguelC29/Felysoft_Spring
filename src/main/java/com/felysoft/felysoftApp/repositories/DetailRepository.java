package com.felysoft.felysoftApp.repositories;

import com.felysoft.felysoftApp.entities.Detail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailRepository extends JpaRepository <Detail, Long> {

}