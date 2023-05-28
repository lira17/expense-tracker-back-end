package com.lira17.expensetracker.report.service;

import com.lira17.expensetracker.dto.get.CategoryGetDto;
import com.lira17.expensetracker.model.BaseBalanceEntity;
import com.lira17.expensetracker.model.Expense;
import com.lira17.expensetracker.model.ExpenseCategory;
import com.lira17.expensetracker.model.Income;
import com.lira17.expensetracker.model.IncomeCategory;
import com.lira17.expensetracker.report.model.FullReport;
import com.lira17.expensetracker.report.model.Report;
import com.lira17.expensetracker.report.model.ReportBalanceEntity;
import com.lira17.expensetracker.service.ExpenseService;
import com.lira17.expensetracker.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lira17.expensetracker.report.util.ReportUtil.isBalancePositive;

@Service
public class ReportService {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private IncomeService incomeService;

    public Report getReportForMonth(Integer month, Integer year) {
        var expenses = expenseService.getMonthlyExpenses(month, year);
        var incomes = incomeService.getMonthlyIncomes(month, year);
        var report = getReport(expenses, incomes);

        report.setYear(year);
        report.setMonth(month);

        return report;
    }

    public FullReport getFullReportForMonth(Integer month, Integer year) {
        var expenses = expenseService.getMonthlyExpenses(month, year);
        var incomes = incomeService.getMonthlyIncomes(month, year);
        var fullReport = getFullReport(expenses, incomes);

        fullReport.setYear(year);
        fullReport.setMonth(month);

        return fullReport;
    }

    public FullReport getFullReportForYear(Integer year) {
        var expenses = expenseService.getYearExpenses(year);
        var incomes = incomeService.getYearIncomes(year);
        var fullReport = getFullReport(expenses, incomes);

        fullReport.setYear(year);

        return fullReport;
    }

    private FullReport getFullReport(List<Expense> expenses, List<Income> incomes) {
        var report = getReport(expenses, incomes);
        var fullReport = new FullReport(report);

        fullReport.setExpenses(getExpensesByCategories(expenses));
        fullReport.setIncomes(getIncomesByCategories(incomes));

        populateCategoryPercent(fullReport.getExpenses(), fullReport.getTotalExpense());
        populateCategoryPercent(fullReport.getIncomes(), report.getTotalIncome());

        return fullReport;
    }

    public Report getReport(List<Expense> expenses, List<Income> incomes) {
        var report = new Report();
        var totalExpense = getTotal(expenses);
        var totalIncome = getTotal(incomes);
        var difference = totalIncome - totalExpense;

        report.setTotalExpense(totalExpense);
        report.setTotalIncome(totalIncome);
        report.setBalancePositive(isBalancePositive(difference));
        report.setDifference(difference);

        return report;
    }

    private void populateCategoryPercent(List<ReportBalanceEntity> list, double totalAmount) {
        list.forEach(reportBalanceEntity ->
                reportBalanceEntity.setCategoryPercent(
                        getCategoryPercent(reportBalanceEntity.getCategoryTotalAmount(), totalAmount)));
    }

    private List<ReportBalanceEntity> getExpensesByCategories(List<Expense> expenses) {
        Map<ExpenseCategory, List<Expense>> map = expenses.stream().collect(Collectors.groupingBy(Expense::getCategory));
        return map.entrySet()
                .stream()
                .map(expenseCategoryListEntry ->
                        ReportBalanceEntity.builder()
                                .category(new CategoryGetDto(expenseCategoryListEntry.getKey().getId(), expenseCategoryListEntry.getKey().getTitle()))
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
                                .category(new CategoryGetDto(expenseCategoryListEntry.getKey().getId(), expenseCategoryListEntry.getKey().getTitle()))
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

    private double getTotal(List<? extends BaseBalanceEntity> entities) {
        return entities.stream().map(BaseBalanceEntity::getAmountInMainCurrency).reduce(Double::sum).orElse(0.0);
    }
}
