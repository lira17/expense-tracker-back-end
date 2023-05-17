package com.lira17.expensetracker.service;

import com.lira17.expensetracker.exception.NotFoundException;
import com.lira17.expensetracker.model.Income;
import com.lira17.expensetracker.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IncomeService {

    @Autowired
    IncomeRepository incomeRepository;


    @Transactional(readOnly = true)
    public Page<Income> getAllIncomes(Pageable pageable) {
        return incomeRepository.findAll(pageable);
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
        return incomeRepository.findById(id).orElseThrow(() -> new NotFoundException(Income.class));
    }

    @Transactional
    public Income addIncome(Income income) {
        return incomeRepository.save(income);
    }

    @Transactional
    public Income updateIncome(long id, Income newIncome) {
        Income oldIncome = getIncomeById(id);
        return incomeRepository.save(updateOldIncome(newIncome, oldIncome));
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

    @Transactional(readOnly = true)
    public Double getTotalIncomes() {
        return incomeRepository.getTotalIncomesAmount().orElse(0.0);
    }

    private Income updateOldIncome(Income newIncome, Income oldIncome) {
        oldIncome.setDate(newIncome.getDate());
        oldIncome.setAmount(newIncome.getAmount());
        oldIncome.setRate(newIncome.getRate());
        oldIncome.setMonth(newIncome.getMonth());
        oldIncome.setYear(newIncome.getYear());
        oldIncome.setCategory(newIncome.getCategory());
        oldIncome.setCurrency(newIncome.getCurrency());
        oldIncome.setAmountInMainCurrency(newIncome.getAmountInMainCurrency());
        oldIncome.setDescription(newIncome.getDescription());
        oldIncome.setTitle(newIncome.getTitle());
        oldIncome.setMainCurrency(newIncome.getMainCurrency());
        return oldIncome;
    }
}
