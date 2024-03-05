package com.felysoft.felysoftApp.services.imp;

import com.felysoft.felysoftApp.entities.Expense;
import com.felysoft.felysoftApp.repositories.ExpenseRepository;
import com.felysoft.felysoftApp.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseImp implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Override
    public List<Expense> findAll() throws Exception {
        return this.expenseRepository.findAll();
    }

    @Override
    public Expense findById(Long id) {
        return this.expenseRepository.findById(id).orElse(null);
    }

    @Override
    public void create(Expense expense) {
        this.expenseRepository.save(expense);
    }

    @Override
    public void update(Expense expense) {
        this.expenseRepository.save(expense);
    }

    @Override
    public void delete(Expense expense) {
        this.expenseRepository.delete(expense);
    }
}
