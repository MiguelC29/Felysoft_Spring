package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.NoveltyInv;

import java.util.List;

public interface NoveltyInvService {
    public List<NoveltyInv> findAll() throws Exception;
    public NoveltyInv findById(Long id);
    public void create(NoveltyInv noveltyInv);
    public void update(NoveltyInv noveltyInv);
    public void delete(NoveltyInv noveltyInv);
}