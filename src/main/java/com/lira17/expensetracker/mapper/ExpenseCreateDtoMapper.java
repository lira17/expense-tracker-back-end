package com.lira17.expensetracker.mapper;

import com.lira17.expensetracker.dto.create.ExpenseCreateDto;
import com.lira17.expensetracker.exchange.ExchangeService;
import com.lira17.expensetracker.model.Expense;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class ExpenseCreateDtoMapper implements DtoMapper<Expense, ExpenseCreateDto>{

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ExchangeService exchangeService;

    @Override
    public ExpenseCreateDto mapToDto(Expense model) {
        return null;
    }

    @Override
    public Expense mapToModel(ExpenseCreateDto dto) {
        var expense = modelMapper.map(dto, Expense.class);
        if (expense.getDate() == null) {
            expense.setDate(LocalDate.now());
        }
        expense.setMonth(expense.getDate().getMonth().getValue());
        expense.setYear(expense.getDate().getYear());

        expense.setMainCurrency(exchangeService.getMainCurrency());
        expense.setAmountInMainCurrency(dto.getAmount());

        if (!expense.getMainCurrency().equals(expense.getCurrency())) {
            var exchange = exchangeService.getExchange(
                    dto.getAmount(),
                    dto.getCurrency().name(),
                    expense.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE));

            expense.setAmountInMainCurrency(exchange.result());
            expense.setRate(exchange.info().rate());
        }

        return expense;
    }
}
