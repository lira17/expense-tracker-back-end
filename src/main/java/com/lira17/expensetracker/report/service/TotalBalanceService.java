package com.lira17.expensetracker.report.service;

import com.lira17.expensetracker.exchange.ExchangeService;
import com.lira17.expensetracker.report.model.TotalBalance;
import com.lira17.expensetracker.service.ExpenseService;
import com.lira17.expensetracker.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.lira17.expensetracker.report.util.ReportUtil.isBalancePositive;

@Service
public class TotalBalanceService {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private ExchangeService exchangeService;


    @Transactional(readOnly = true)
    public TotalBalance getTotalBalance() {
        double balanceAmount = incomeService.getTotalIncomes() - expenseService.getTotalExpenses();
        return new TotalBalance(balanceAmount, exchangeService.getMainCurrency(), isBalancePositive(balanceAmount));
    }
}
