package com.felysoft.felysoftApp.service.imp;

import com.felysoft.felysoftApp.entity.User;
import com.felysoft.felysoftApp.repository.UserRepository;
import com.felysoft.felysoftApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserImp implements UserService, UserDetailsService {

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByEmailAndEliminatedFalse(username).orElseThrow();
    }
}
