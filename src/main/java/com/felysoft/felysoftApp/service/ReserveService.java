package com.felysoft.felysoftApp.service;

import com.felysoft.felysoftApp.entity.Reserve;
import com.felysoft.felysoftApp.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReserveService {
    List<Reserve> findAll() throws Exception;
    List<Reserve> findAllDisabled() throws Exception;
    List<Reserve> findReservesByUser(User user) throws Exception;

    Reserve findById(Long id);
    Reserve findByIdDisabled(Long id);

    @Transactional
    void create(Reserve reserve);

    @Transactional
    @Modifying
    void update(Reserve reserve);

    @Transactional
    @Modifying
    void delete(Reserve reserve);
    @Transactional
    @Modifying
    void cancel(Reserve reserve);
}
