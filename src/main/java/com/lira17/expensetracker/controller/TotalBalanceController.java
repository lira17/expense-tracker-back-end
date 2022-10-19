package com.lira17.expensetracker.controller;

import com.lira17.expensetracker.report.model.TotalBalance;
import com.lira17.expensetracker.report.service.TotalBalanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.lira17.expensetracker.common.Constants.API;

@RestController
@Tag(name = "Total Balance", description = "Total Balance API")
@RequestMapping(API + "total-balance")
public class TotalBalanceController {

    @Autowired
    private TotalBalanceService totalBalanceService;

    @GetMapping
    @Operation(summary = "Get total balance")
    public TotalBalance getTotalBalance() {
        return totalBalanceService.getTotalBalance();
    }
}
