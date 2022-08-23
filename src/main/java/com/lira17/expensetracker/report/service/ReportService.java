package com.lira17.expensetracker.report.service;

import com.lira17.expensetracker.dto.get.CategoryGetDto;
import com.lira17.expensetracker.model.Expense;
import com.lira17.expensetracker.model.ExpenseCategory;
import com.lira17.expensetracker.model.Income;
import com.lira17.expensetracker.model.IncomeCategory;
import com.lira17.expensetracker.report.model.Report;
import com.lira17.expensetracker.report.model.ReportBalanceEntity;
import com.lira17.expensetracker.service.ExpenseService;
import com.lira17.expensetracker.service.IncomeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private ModelMapper modelMapper;

    public Report getReport(Integer month, Integer year) {
        var report = new Report();
        report.setYear(year);
        report.setMonth(month);

        var expenses = expenseService.getExpensesForReport(month, year);
        var incomes = incomeService.getIncomesForReport(month, year);

        var totalExpense = expenses.stream().map(Expense::getAmountInMainCurrency).reduce(Double::sum).orElseThrow();
        var totalIncome = incomes.stream().map(Income::getAmountInMainCurrency).reduce(Double::sum).orElseThrow();
        var difference = totalIncome - totalExpense;

        report.setTotalExpense(totalExpense);
        report.setTotalIncome(totalIncome);
        report.setBalancePositive(difference > 0);
        report.setDifference(difference);
        report.setExpenses(getExpensesByCategories(expenses));
        report.setIncomes(getIncomesByCategories(incomes));

        populateCategoryPercent(report.getExpenses(), report.getTotalExpense());
        populateCategoryPercent(report.getIncomes(), report.getTotalIncome());

        return report;
    }

    private void populateCategoryPercent(List<ReportBalanceEntity> list, double totalAmount) {
        list.forEach(reportBalanceEntity -> reportBalanceEntity.setCategoryPercent(getCategoryPercent(reportBalanceEntity.getCategoryTotalAmount(), totalAmount)));
    }

    private List<ReportBalanceEntity> getExpensesByCategories(List<Expense> expenses) {
        Map<ExpenseCategory, List<Expense>> map = expenses.stream().collect(Collectors.groupingBy(Expense::getCategory));
        return map.entrySet()
                .stream()
                .map(expenseCategoryListEntry ->
                        ReportBalanceEntity.builder()
                                .category(modelMapper.map(expenseCategoryListEntry.getKey(), CategoryGetDto.class))
                                .categoryTotalAmount(expenseCategoryListEntry.getValue()
                                        .stream()
                                        .map(Expense::getAmountInMainCurrency)
                                        .reduce(Double::sum).orElseThrow())
                                .build())
                .toList();
    }

    private List<ReportBalanceEntity> getIncomesByCategories(List<Income> incomes) {
        Map<IncomeCategory, List<Income>> map = incomes.stream().collect(Collectors.groupingBy(Income::getCategory));
        return map.entrySet()
                .stream()
                .map(expenseCategoryListEntry ->
                        ReportBalanceEntity.builder()
                                .category(modelMapper.map(expenseCategoryListEntry.getKey(), CategoryGetDto.class))
                                .categoryTotalAmount(expenseCategoryListEntry.getValue()
                                        .stream()
                                        .map(Income::getAmountInMainCurrency)
                                        .reduce(Double::sum).orElseThrow())
                                .build())
                .toList();
    }

    private double getCategoryPercent(double categoryAmount, double totalAmount) {
        return categoryAmount / totalAmount * 100;
    }
}
