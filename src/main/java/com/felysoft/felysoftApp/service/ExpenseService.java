package com.felysoft.felysoftApp.service;

import com.felysoft.felysoftApp.entity.Expense;
import com.felysoft.felysoftApp.entity.Purchase;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ExpenseService {
    List<Expense> findAll() throws Exception;

    Expense findById(Long id);

    Expense findByPurchase(Purchase purchase);

    @Transactional
    void create(Expense expense);

    @Transactional
    @Modifying
    void update(Expense expense);

    @Transactional
    @Modifying
    void delete(Expense expense);
}
