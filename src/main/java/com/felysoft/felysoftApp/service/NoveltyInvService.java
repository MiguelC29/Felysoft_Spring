package com.felysoft.felysoftApp.service;

import com.felysoft.felysoftApp.entity.NoveltyInv;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NoveltyInvService {
    List<NoveltyInv> findAll() throws Exception;

    NoveltyInv findById(Long id);

    @Transactional
    void create(NoveltyInv noveltyInv);

    @Transactional
    @Modifying
    void update(NoveltyInv noveltyInv);

    @Transactional
    @Modifying
    void delete(NoveltyInv noveltyInv);
}
