package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Genre;

import java.util.List;

public interface GenreService {
    public List<Genre> findAll() throws Exception;
    public Genre findById(Long id);
    public void create(Genre genre);
    public void update(Genre genre);
    public void delete(Genre genre);
}