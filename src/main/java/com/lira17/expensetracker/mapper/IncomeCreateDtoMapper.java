package com.lira17.expensetracker.mapper;

import com.lira17.expensetracker.dto.create.IncomeCreateDto;
import com.lira17.expensetracker.exchange.ExchangeService;
import com.lira17.expensetracker.model.Income;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class IncomeCreateDtoMapper implements DtoMapper<Income, IncomeCreateDto>{

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ExchangeService exchangeService;

    @Override
    public IncomeCreateDto mapToDto(Income model) {
        return null;
    }

    @Override
    public Income mapToModel(IncomeCreateDto dto) {
        var income = modelMapper.map(dto, Income.class);
        if (income.getDate() == null) {
            income.setDate(LocalDate.now());
        }
        income.setMonth(income.getDate().getMonth().getValue());
        income.setYear(income.getDate().getYear());
        income.setMainCurrency(exchangeService.getMainCurrency());
        income.setAmountInMainCurrency(dto.getAmount());

        if (!income.getMainCurrency().equals(income.getCurrency())) {
            var exchange = exchangeService.getExchange(
                    dto.getAmount(),
                    dto.getCurrency().name(),
                    income.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE));

            income.setAmountInMainCurrency(exchange.result());
            income.setRate(exchange.info().rate());
        }

        return income;
    }
}
