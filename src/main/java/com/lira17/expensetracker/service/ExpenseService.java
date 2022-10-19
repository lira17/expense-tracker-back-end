package com.lira17.expensetracker.service;

import com.lira17.expensetracker.exception.NotFoundException;
import com.lira17.expensetracker.model.Expense;
import com.lira17.expensetracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    ExpenseRepository expenseRepository;


    @Transactional(readOnly = true)
    public Page<Expense> getAllExpenses(Pageable pageable) {
        return expenseRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<Expense> getMonthlyExpenses(int month, int year) {
        return expenseRepository.findByMonthAndYearOrderByDate(month, year);
    }

    @Transactional(readOnly = true)
    public List<Expense> getYearExpenses(int year) {
        return expenseRepository.findByYearOrderByDate(year);
    }

    @Transactional(readOnly = true)
    public Expense getExpenseById(long id) {
        return expenseRepository.findById(id).orElseThrow(() -> new NotFoundException(Expense.class));
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

    @Transactional(readOnly = true)
    public List<Expense> getExpensesForReport(Integer month, Integer year) {
        return month != null
                ? getMonthlyExpenses(month, year)
                : getYearExpenses(year);
    }

    @Transactional(readOnly = true)
    public Double getTotalExpenses() {
       return expenseRepository.getTotalExpensesAmount().orElse(0.0);
    }

}
