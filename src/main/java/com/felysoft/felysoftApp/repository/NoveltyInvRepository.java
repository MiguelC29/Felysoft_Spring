package com.felysoft.felysoftApp.repository;

import com.felysoft.felysoftApp.entity.NoveltyInv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoveltyInvRepository extends JpaRepository<NoveltyInv, Long> {
    List<NoveltyInv> findNoveltyInvsByEliminatedFalse();

    NoveltyInv findNoveltyInvByIdNoveltyAndEliminatedFalse(Long id);
}
