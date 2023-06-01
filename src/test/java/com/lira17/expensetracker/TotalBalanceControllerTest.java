package com.lira17.expensetracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lira17.expensetracker.common.ExpenseCategoryType;
import com.lira17.expensetracker.dto.create.ExpenseCreateDto;
import com.lira17.expensetracker.dto.get.ExpenseCategoryGetDto;
import com.lira17.expensetracker.dto.get.ExpenseGetDto;
import com.lira17.expensetracker.exchange.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(initializers = {TestContainersInitializer.class})
public class TotalBalanceControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    private static final String TOTAL_BALANCE_API_URL = "/api/total-balance";
    private static final String EXPENSE_API_URL = "/api/expenses";


    @Test
    void getTotalBalance_whenBalanceIsPositive_thenReturnsDtoWithPositiveBalance() throws Exception {
        mockMvc.perform(get(TOTAL_BALANCE_API_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.balanceAmount").value("432000.0"))
                .andExpect(jsonPath("$.mainCurrency").value("RSD"))
                .andExpect(jsonPath("$.isBalancePositive").value("true"))
                .andReturn();
    }

    @Test
    void getTotalBalance_whenBalanceIsNegative_thenReturnsDtoWithNegativeBalance() throws Exception {
        //given - adding new expense
        MvcResult result = mockMvc.perform(post(EXPENSE_API_URL)
                        .content(mapper.writeValueAsString(new ExpenseCreateDto("Apartment", "Bough", LocalDate.now(), 450000.0, Currency.RSD, new ExpenseCategoryGetDto(1, "", ExpenseCategoryType.ESSENTIAL))))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        var expense = mapper.readValue(result.getResponse().getContentAsString(), ExpenseGetDto.class);

        //when and then
        mockMvc.perform(get(TOTAL_BALANCE_API_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.balanceAmount").value("-18000.0"))
                .andExpect(jsonPath("$.mainCurrency").value("RSD"))
                .andExpect(jsonPath("$.isBalancePositive").value("false"))
                .andReturn();

        //clean up
        mockMvc.perform(delete(EXPENSE_API_URL + "/" + expense.id()))
                .andExpect(status().isOk());
    }
}


