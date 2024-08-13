package com.felysoft.felysoftApp.service;

import com.felysoft.felysoftApp.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    List<User> findAll() throws Exception;

    List<User> findAllDisabled() throws Exception;

    User findById(Long id);

    User findByIdDisabled(Long id);

    User validateUser(String email, String password);

    User findByNumIdentification(Long numIdentification);

    @Transactional
    void create(User user);

    @Transactional
    @Modifying
    void update(User user);

    @Transactional
    @Modifying
    void delete(User user);
}
