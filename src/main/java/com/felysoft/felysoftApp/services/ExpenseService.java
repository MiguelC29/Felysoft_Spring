package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Expense;
import java.util.List;

public interface ExpenseService {

    public List<Expense> findAll() throws Exception;

    public void create(Expense expense);

    public void update(Expense expense);

    public void delete(Expense expense);
}
