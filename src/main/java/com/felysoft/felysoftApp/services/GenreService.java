package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Genre;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GenreService {
    public List<Genre> findAll() throws Exception;
    public Genre findById(Long id);
    @Transactional
    public void create(Genre genre);
    @Transactional
    @Modifying
    public void update(Genre genre);
    @Transactional
    @Modifying
    public void delete(Genre genre);
}