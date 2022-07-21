package com.lira17.expensetracker.mapper;

import com.lira17.expensetracker.exchange.ExchangeService;
import com.lira17.expensetracker.model.BaseBalanceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class BaseBalanceEntityMapper {

    @Autowired
    ExchangeService exchangeService;

    protected void populateBaseBalanceEntity(BaseBalanceEntity balanceEntity) {
        if (balanceEntity.getDate() == null) {
            balanceEntity.setDate(LocalDate.now());
        }
        balanceEntity.setMonth(balanceEntity.getDate().getMonth().getValue());
        balanceEntity.setYear(balanceEntity.getDate().getYear());

        balanceEntity.setMainCurrency(exchangeService.getMainCurrency());
        balanceEntity.setAmountInMainCurrency(balanceEntity.getAmount());

        if (isDifferentBalanceCurrency(balanceEntity)) {
            var exchange = exchangeService.getExchange(
                    balanceEntity.getAmount(),
                    balanceEntity.getCurrency().name(),
                    balanceEntity.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE));

            balanceEntity.setAmountInMainCurrency(exchange.result());
            balanceEntity.setRate(exchange.info().rate());
        }
    }

    private boolean isDifferentBalanceCurrency(BaseBalanceEntity balanceEntity) {
        return !balanceEntity.getMainCurrency().equals(balanceEntity.getCurrency());
    }


}
