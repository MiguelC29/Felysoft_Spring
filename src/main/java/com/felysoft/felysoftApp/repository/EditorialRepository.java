package com.felysoft.felysoftApp.repository;

import com.felysoft.felysoftApp.entity.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EditorialRepository  extends JpaRepository<Editorial,Long> {
    List<Editorial> findEditorialsByEliminatedFalse();
    List<Editorial> findEditorialsByEliminatedTrue();


    Editorial findEditorialByIdEditorialAndEliminatedFalse(Long id);
    Editorial findEditorialByIdEditorialAndEliminatedTrue(Long id);
    Editorial findEditorialByNameAndEliminatedTrue(String name);
}
