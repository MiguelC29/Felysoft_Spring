package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Expense;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ExpenseService {
    public List<Expense> findAll() throws Exception;
    public Expense findById(Long id);

    @Transactional
    public void create(Expense expense);

    @Transactional
    @Modifying
    public void update(Expense expense);

    @Transactional
    @Modifying
    public void delete(Expense expense);
}