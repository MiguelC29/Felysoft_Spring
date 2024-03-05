package com.felysoft.felysoftApp.services.imp;

import com.felysoft.felysoftApp.entities.NoveltyInv;
import com.felysoft.felysoftApp.repositories.NoveltyInvRepository;
import com.felysoft.felysoftApp.services.NoveltyInvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoveltyInvImp implements NoveltyInvService {

    @Autowired
    private NoveltyInvRepository noveltyInvRepository;

    @Override
    public List<NoveltyInv> findAll() throws Exception {
        return this.noveltyInvRepository.findAll();
    }

    @Override
    public NoveltyInv findById(Long id) {
        return this.noveltyInvRepository.findById(id).orElse(null);
    }

    @Override
    public void create(NoveltyInv noveltyInv) {
        this.noveltyInvRepository.save(noveltyInv);
    }

    @Override
    public void update(NoveltyInv noveltyInv) {
        this.noveltyInvRepository.save(noveltyInv);
    }

    @Override
    public void delete(NoveltyInv noveltyInv) {
        this.noveltyInvRepository.delete(noveltyInv);
    }
}