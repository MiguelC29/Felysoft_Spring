package com.felysoft.felysoftApp.repositories;

import com.felysoft.felysoftApp.entities.NoveltyInv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoveltyInvRepository extends JpaRepository<NoveltyInv, Long> {

    List<NoveltyInv> findNoveltyInvsByEliminatedFalse();
    NoveltyInv findNoveltyInvByIdNoveltyAndEliminatedFalse(Long id);
}