package com.felysoft.felysoftApp.service.imp;

import com.felysoft.felysoftApp.entity.Expense;
import com.felysoft.felysoftApp.entity.Purchase;
import com.felysoft.felysoftApp.repository.ExpenseRepository;
import com.felysoft.felysoftApp.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseImp implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Override
    public List<Expense> findAll() throws Exception {
        return this.expenseRepository.findExpenseByEliminatedFalse();
    }

    @Override
    public List<Expense> findAllDisabled() throws Exception {
        return this.expenseRepository.findExpenseByEliminatedTrue();
    }

    @Override
    public Expense findById(Long id) {
        return this.expenseRepository.findExpenseByIdExpenseAndEliminatedFalse(id);
    }

    @Override
    public Expense findByIdDisabled(Long id) {
        return this.expenseRepository.findExpenseByIdExpenseAndEliminatedTrue(id);
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
        this.expenseRepository.save(expense);
    }
}
