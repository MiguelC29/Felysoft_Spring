package com.felysoft.felysoftApp.repository;

import com.felysoft.felysoftApp.entity.Expense;
import com.felysoft.felysoftApp.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findExpenseByEliminatedFalse();
    List<Expense> findExpenseByEliminatedTrue();


    Expense findExpenseByIdExpenseAndEliminatedFalse(Long id);
    Expense findExpenseByIdExpenseAndEliminatedTrue(Long id);
}
