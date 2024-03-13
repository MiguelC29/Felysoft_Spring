package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.NoveltyInv;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NoveltyInvService {
    public List<NoveltyInv> findAll() throws Exception;
    public NoveltyInv findById(Long id);

    @Transactional
    public void create(NoveltyInv noveltyInv);

    @Transactional
    @Modifying
    public void update(NoveltyInv noveltyInv);

    @Transactional
    @Modifying
    public void delete(NoveltyInv noveltyInv);
}