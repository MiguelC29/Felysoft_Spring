package com.felysoft.felysoftApp.repository;

import com.felysoft.felysoftApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndEliminatedFalse(String email);

    List<User> findUsersByEliminatedFalse();

    User findUserByIdUserAndEliminatedFalse(Long id);

    User findUserByEmailAndPasswordAndEliminatedFalse(String email, String password);
}
