package com.lira17.expensetracker.service;

import com.lira17.expensetracker.model.Expense;
import com.lira17.expensetracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    ExpenseRepository expenseRepository;

    @Transactional(readOnly = true)
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Expense getExpenseById(long id) {
        return expenseRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Expense addExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Transactional
    public void deleteExpense(long id) {
        var expense = getExpenseById(id);
        expenseRepository.delete(expense);
    }
}
