package com.lira17.expensetracker.service;

import com.lira17.expensetracker.model.Income;
import com.lira17.expensetracker.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IncomeService {

    @Autowired
    IncomeRepository incomeRepository;

    @Transactional(readOnly = true)
    public List<Income> getAllIncomes() {
        return incomeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Income> getMonthlyIncomes(int month, int year) {
        return incomeRepository.findByMonthAndYearOrderByDate(month, year);
    }

    @Transactional(readOnly = true)
    public List<Income> getYearIncomes(int year) {
        return incomeRepository.findByYearOrderByDate(year);
    }

    @Transactional(readOnly = true)
    public Income getIncomeById(long id) {
        return incomeRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Income addIncome(Income income) {
        return incomeRepository.save(income);
    }

    @Transactional
    public void deleteIncome(long id) {
        var income = getIncomeById(id);
        incomeRepository.delete(income);
    }

    @Transactional(readOnly = true)
    public List<Income> getIncomesForReport(Integer month, Integer year) {
        return month != null
                ? getMonthlyIncomes(month, year)
                : getYearIncomes(year);
    }
}
