package com.felysoft.felysoftApp.service.imp;

import com.felysoft.felysoftApp.entity.NoveltyInv;
import com.felysoft.felysoftApp.repository.NoveltyInvRepository;
import com.felysoft.felysoftApp.service.NoveltyInvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoveltyInvImp implements NoveltyInvService {

    @Autowired
    private NoveltyInvRepository noveltyInvRepository;

    @Override
    public List<NoveltyInv> findAll() throws Exception {
        return this.noveltyInvRepository.findNoveltyInvsByEliminatedFalse();
    }

    @Override
    public NoveltyInv findById(Long id) {
        return this.noveltyInvRepository.findNoveltyInvByIdNoveltyAndEliminatedFalse(id);
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
        this.noveltyInvRepository.save(noveltyInv);
    }
}
