package com.felysoft.felysoftApp.repositories;

import com.felysoft.felysoftApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findUsersByEliminatedFalse();

    User findUserByIdUserAndEliminatedFalse(Long id);

    Optional<User> findByUsername(String username);
}