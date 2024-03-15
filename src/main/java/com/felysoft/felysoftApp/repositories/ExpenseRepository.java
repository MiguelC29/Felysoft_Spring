package com.felysoft.felysoftApp.repositories;

import com.felysoft.felysoftApp.entities.Expense;
import com.felysoft.felysoftApp.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findExpenseByEliminatedFalse();

    Expense findExpenseByIdExpenseAndEliminatedFalse(Long id);
}