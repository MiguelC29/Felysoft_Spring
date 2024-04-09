package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    public List<User> findAll() throws Exception;
    public User findById(Long id);
    public User validateUser(String email, String password);
    @Transactional
    public void create(User user);
    @Transactional
    @Modifying
    public void update(User user);
    @Transactional
    @Modifying
    public void delete(User user);
}