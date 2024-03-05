package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.User;

import java.util.List;

public interface UserService {
    public List<User> findAll() throws Exception;
    public void create(User user);
    public void update(User user);
    public void delete(User user);
}