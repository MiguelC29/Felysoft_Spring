package com.felysoft.felysoftApp.service.imp;

import com.felysoft.felysoftApp.entity.Role;
import com.felysoft.felysoftApp.entity.User;
import com.felysoft.felysoftApp.repository.UserRepository;
import com.felysoft.felysoftApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserImp implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> findAll() throws Exception {
        return this.userRepository.findUsersByEliminatedFalse();
    }

    @Override
    public List<User> findAllDisabled() throws Exception {
        return this.userRepository.findUsersByEliminatedTrue();
    }

    @Override
    public List<User> findByRole(Role role) {
        return this.userRepository.findUsersByRoleAndEliminatedFalseAndEnabledTrue(role);
    }

    @Override
    public User findById(Long id) {
        return this.userRepository.findUserByIdUserAndEliminatedFalse(id);
    }

    @Override
    public User findByIdDisabled(Long id) {
        return this.userRepository.findUserByIdUserAndEliminatedTrue(id);
    }

    @Override
    public User validateUser(String email, String password) {
        return this.userRepository.findUserByEmailAndPasswordAndEliminatedFalse(email, password);
    }

    @Override
    public User findByNumIdentification(Long numIdentification) {
        return this.userRepository.findUserByNumIdentificationAndEliminatedTrue(numIdentification);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return this.userRepository.findByEmailAndEliminatedFalse(email);
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
