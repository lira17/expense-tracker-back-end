package com.lira17.expensetracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lira17.expensetracker.dto.create.ExpenseCreateDto;
import com.lira17.expensetracker.dto.get.ExpenseGetDto;
import com.lira17.expensetracker.exchange.Currency;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(initializers = {TestContainersInitializer.class})
public class ExpenseControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    private static final String EXPENSE_API_URL = "/api/expenses";

    @Test
    void getAllExpensesRequest_whenGet_thenReturnsDtoWithExpenses() throws Exception {
        mockMvc.perform(get(EXPENSE_API_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[0].id").value("1"))
                .andExpect(jsonPath("$.[0].title").value("January rent"))
                .andExpect(jsonPath("$.[0].rate").value("1.2"))
                .andExpect(jsonPath("$.[0].currency").value("EUR"))
                .andExpect(jsonPath("$.[0].mainCurrency").value("RSD"))
                .andExpect(jsonPath("$.[0].amount").value("100.0"))
                .andExpect(jsonPath("$.[0].amountInMainCurrency").value("12000.0"))
                .andExpect(jsonPath("$.[3].id").value("4"))
                .andExpect(jsonPath("$.[3].title").value("Green day concert"))
                .andExpect(jsonPath("$.[0].rate").value("1.2"))
                .andExpect(jsonPath("$.[0].currency").value("EUR"))
                .andExpect(jsonPath("$.[0].mainCurrency").value("RSD"))
                .andExpect(jsonPath("$.[3].amount").value("100.0"))
                .andExpect(jsonPath("$.[3].amountInMainCurrency").value("12000.0"))
                .andReturn();
    }


    @Test
    void getExpenseById_whenIdIsPresent_thenReturnsDtoWithExpense() throws Exception {
        mockMvc.perform(get(getRequestUrl(1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("January rent"))
                .andExpect(jsonPath("$.rate").value("1.2"))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andExpect(jsonPath("$.mainCurrency").value("RSD"))
                .andExpect(jsonPath("$.amount").value("100.0"))
                .andExpect(jsonPath("$.amountInMainCurrency").value("12000.0"))
                .andReturn();
    }

    @Ignore
    @Test
    void getExpenseById_whenIdIsNotFound_thenReturnsNotFoundError() throws Exception {
        mockMvc.perform(get(getRequestUrl(100)))
                .andExpect(status().isNotFound())
                .andReturn();
    }


    @Test
    void createExpense_whenExpenseDtoWithMainCurrency_thenCreatesExpense() throws Exception {
        //when
        MvcResult result = mockMvc
                .perform(post(EXPENSE_API_URL)
                        .content(mapper.writeValueAsString(new ExpenseCreateDto("Beer", "Friday beer", LocalDate.now(), 500.0, Currency.RSD, 2)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        var expenseGetDto = mapper.readValue(result.getResponse().getContentAsString(), ExpenseGetDto.class);

        //then
        assertEquals(expenseGetDto.currency(), expenseGetDto.mainCurrency());
        assertEquals(expenseGetDto.amount(), expenseGetDto.amountInMainCurrency());
        mockMvc.perform(get(getRequestUrl(expenseGetDto.id())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(expenseGetDto.id()))
                .andExpect(jsonPath("$.title").value(expenseGetDto.title()))
                .andExpect(jsonPath("$.amount").value(expenseGetDto.amount()))
                .andExpect(jsonPath("$.amountInMainCurrency").value(expenseGetDto.amountInMainCurrency()))
                .andExpect(jsonPath("$.currency").value(expenseGetDto.currency().toString()))
                .andExpect(jsonPath("$.mainCurrency").value(expenseGetDto.mainCurrency().toString()))
                .andExpect(jsonPath("$.category.id").value(expenseGetDto.category().id()))
                .andExpect(jsonPath("$.category.title").value(expenseGetDto.category().title()))
                .andExpect(jsonPath("$.category.type").value(expenseGetDto.category().type().toString()))
                .andReturn();

        //clean up
        mockMvc.perform(delete(getRequestUrl(expenseGetDto.id())))
                .andExpect(status().isOk());
    }

    @Test
    void createExpense_whenExpenseDtoWithOtherCurrency_thenCreatesExpense() throws Exception {
        //when
        MvcResult result = mockMvc
                .perform(post(EXPENSE_API_URL)
                        .content(mapper.writeValueAsString(new ExpenseCreateDto("Wine", "Wednesday wine", LocalDate.now(), 5.0, Currency.EUR, 2)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        var expenseGetDto = mapper.readValue(result.getResponse().getContentAsString(), ExpenseGetDto.class);

        //then
        assertNotEquals(expenseGetDto.currency(), expenseGetDto.mainCurrency());
        assertNotEquals(expenseGetDto.amount(), expenseGetDto.amountInMainCurrency());
        assertEquals(expenseGetDto.amount() * expenseGetDto.rate(), expenseGetDto.amountInMainCurrency());
        mockMvc.perform(get(getRequestUrl(expenseGetDto.id())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(expenseGetDto.id()))
                .andExpect(jsonPath("$.title").value(expenseGetDto.title()))
                .andExpect(jsonPath("$.amount").value(expenseGetDto.amount()))
                .andExpect(jsonPath("$.amountInMainCurrency").value(expenseGetDto.amountInMainCurrency()))
                .andExpect(jsonPath("$.currency").value(expenseGetDto.currency().toString()))
                .andExpect(jsonPath("$.mainCurrency").value(expenseGetDto.mainCurrency().toString()))
                .andExpect(jsonPath("$.category.id").value(expenseGetDto.category().id()))
                .andExpect(jsonPath("$.category.title").value(expenseGetDto.category().title()))
                .andExpect(jsonPath("$.category.type").value(expenseGetDto.category().type().toString()))
                .andReturn();

        //clean up
        mockMvc.perform(delete(getRequestUrl(expenseGetDto.id())))
                .andExpect(status().isOk());
    }


    @Test
    void updateExpense_whenExpenseIdIsPresentAndDtoIsOk_thenUpdatesExistingExpense() throws Exception {
        //given - creating new category
        MvcResult result = mockMvc
                .perform(post(EXPENSE_API_URL)
                        .content(mapper.writeValueAsString(new ExpenseCreateDto("Wine", "Wednesday wine", LocalDate.now(), 5.0, Currency.EUR, 2)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        var expenseGetDto = mapper.readValue(result.getResponse().getContentAsString(), ExpenseGetDto.class);

        //when
        MvcResult resultUpdated = mockMvc.perform(put(getRequestUrl(expenseGetDto.id()))
                        .content(mapper.writeValueAsString(new ExpenseCreateDto("Wine", "Wednesday wine", LocalDate.now(), 7.0, Currency.EUR, 2)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        var expenseGetDtoUpdated = mapper.readValue(resultUpdated.getResponse().getContentAsString(), ExpenseGetDto.class);

        //then
        assertNotEquals(expenseGetDtoUpdated.currency(), expenseGetDtoUpdated.mainCurrency());
        assertNotEquals(expenseGetDtoUpdated.amount(), expenseGetDtoUpdated.amountInMainCurrency());
        assertEquals(expenseGetDtoUpdated.amount() * expenseGetDtoUpdated.rate(), expenseGetDtoUpdated.amountInMainCurrency(), 0.001);
        mockMvc.perform(get(getRequestUrl(expenseGetDto.id())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(expenseGetDtoUpdated.id()))
                .andExpect(jsonPath("$.title").value(expenseGetDtoUpdated.title()))
                .andExpect(jsonPath("$.amount").value("7.0"))
                .andExpect(jsonPath("$.amountInMainCurrency").value(expenseGetDtoUpdated.amountInMainCurrency()))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andExpect(jsonPath("$.mainCurrency").value(expenseGetDtoUpdated.mainCurrency().toString()))
                .andExpect(jsonPath("$.category.id").value(expenseGetDtoUpdated.category().id()))
                .andExpect(jsonPath("$.category.title").value(expenseGetDtoUpdated.category().title()))
                .andExpect(jsonPath("$.category.type").value(expenseGetDtoUpdated.category().type().toString()))
                .andReturn();

        //clean up
        mockMvc.perform(delete(getRequestUrl(expenseGetDtoUpdated.id())))
                .andExpect(status().isOk());
    }

    @Test
    void updateExpense_whenExpenseIdIsNotPresentAndDtoIsOk_thenReturnsNotFoundError() throws Exception {
        mockMvc.perform(put(getRequestUrl(107))
                        .content(mapper.writeValueAsString(new ExpenseCreateDto("Wine", "Wednesday wine", LocalDate.now(), 5.0, Currency.EUR, 2)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteExpense_whenExpenseIdIsNotPresent_thenReturnsNotFoundError() throws Exception {
        mockMvc.perform(delete(getRequestUrl(107)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteExpense_whenExpenseIdIsPresent_thenDeletesExpense() throws Exception {
        //given - creating new expense
        MvcResult result = mockMvc
                .perform(post(EXPENSE_API_URL)
                        .content(mapper.writeValueAsString(new ExpenseCreateDto("Wine", "Wednesday wine", LocalDate.now(), 5.0, Currency.EUR, 2)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        var expenseGetDto = mapper.readValue(result.getResponse().getContentAsString(), ExpenseGetDto.class);

        //when
        mockMvc.perform(delete(getRequestUrl(expenseGetDto.id())))
                .andExpect(status().isOk());

        //then
        mockMvc.perform(get(getRequestUrl(expenseGetDto.id())))
                .andExpect(status().isNotFound())
                .andReturn();
    }


    private String getRequestUrl(long pathVariable) {
        return EXPENSE_API_URL + "/" + pathVariable;
    }
}
