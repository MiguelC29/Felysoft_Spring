package com.felysoft.felysoftApp.repositories;

import com.felysoft.felysoftApp.entities.Author;
import com.felysoft.felysoftApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findUsersByEliminatedFalse();

    User findUserByIdUserAndEliminatedFalse(Long id);

    User findUserByEmailAndPasswordAndEliminatedFalse(String email, String password);
}