package com.lira17.expensetracker.repository;

import com.lira17.expensetracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByMonthAndYearOrderByDate(int month, int year);

    List<Expense> findByYearOrderByDate(int year);
}
