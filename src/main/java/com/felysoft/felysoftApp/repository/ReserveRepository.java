package com.felysoft.felysoftApp.repository;

import com.felysoft.felysoftApp.entity.Reserve;
import com.felysoft.felysoftApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReserveRepository extends JpaRepository<Reserve, Long>{
    List<Reserve> findReservesByEliminatedFalse();
    List<Reserve> findReservesByEliminatedTrue();
    List<Reserve> findReservesByEliminatedFalseAndUser(User user);
    List<Reserve> findReservesByEliminatedFalseAndUserAndState(User user, Reserve.State state);
    Reserve findReservesByIdReserveAndEliminatedFalse(Long id);
    Reserve findReservesByIdReserveAndEliminatedTrue(Long id);
}
