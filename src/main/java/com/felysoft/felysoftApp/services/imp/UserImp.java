package com.felysoft.felysoftApp.services.imp;

import com.felysoft.felysoftApp.entities.User;
import com.felysoft.felysoftApp.repositories.UserRepository;
import com.felysoft.felysoftApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserImp implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> findAll() throws Exception {
        return this.userRepository.findUsersByEliminatedFalse();
    }

    @Override
    public User findById(Long id) {
        return this.userRepository.findUserByIdUserAndEliminatedFalse(id);
    }

    @Override
    public User validateUser(String email, String password) {
        return this.userRepository.findUserByEmailAndPasswordAndEliminatedFalse(email, password);
    }

    @Override
    public void create(User user) {
        this.userRepository.save(user);
    }

    @Override
    public void update(User user) {
        this.userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        this.userRepository.save(user);
    }
}