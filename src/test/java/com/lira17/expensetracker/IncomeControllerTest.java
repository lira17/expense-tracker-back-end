package com.lira17.expensetracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lira17.expensetracker.dto.create.IncomeCreateDto;
import com.lira17.expensetracker.dto.get.IncomeGetDto;
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
public class IncomeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    private static final String INCOMES_API_URL = "/api/incomes";

    @Test
    void getAllIncomesRequest_whenGet_thenReturnsDtoWithIncomes() throws Exception {
        mockMvc.perform(get(INCOMES_API_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[0].id").value("1"))
                .andExpect(jsonPath("$.[0].title").value("January salary"))
                .andExpect(jsonPath("$.[0].rate").value("1.2"))
                .andExpect(jsonPath("$.[0].currency").value("EUR"))
                .andExpect(jsonPath("$.[0].mainCurrency").value("RSD"))
                .andExpect(jsonPath("$.[0].amount").value("1000.0"))
                .andExpect(jsonPath("$.[0].amountInMainCurrency").value("120000.0"))
                .andExpect(jsonPath("$.[3].id").value("4"))
                .andExpect(jsonPath("$.[3].title").value("April gift"))
                .andExpect(jsonPath("$.[0].rate").value("1.2"))
                .andExpect(jsonPath("$.[0].currency").value("EUR"))
                .andExpect(jsonPath("$.[0].mainCurrency").value("RSD"))
                .andExpect(jsonPath("$.[3].amount").value("1000.0"))
                .andExpect(jsonPath("$.[3].amountInMainCurrency").value("120000.0"))
                .andReturn();
    }


    @Test
    void getIncomeById_whenIdIsPresent_thenReturnsDtoWithIncome() throws Exception {
        mockMvc.perform(get(getRequestUrl(1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("January salary"))
                .andExpect(jsonPath("$.rate").value("1.2"))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andExpect(jsonPath("$.mainCurrency").value("RSD"))
                .andExpect(jsonPath("$.amount").value("1000.0"))
                .andExpect(jsonPath("$.amountInMainCurrency").value("120000.0"))
                .andReturn();
    }

    @Ignore
    @Test
    void getIncomeById_whenIdIsNotFound_thenReturnsNotFoundError() throws Exception {
        mockMvc.perform(get(getRequestUrl(100)))
                .andExpect(status().isNotFound())
                .andReturn();
    }


    @Test
    void createIncome_whenIncomeDtoWithMainCurrency_thenCreatesIncome() throws Exception {
        //when
        MvcResult result = mockMvc
                .perform(post(INCOMES_API_URL)
                        .content(mapper.writeValueAsString(new IncomeCreateDto("Gift", "gift", LocalDate.now(), 5000.0, Currency.RSD, 3)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        var incomeGetDto = mapper.readValue(result.getResponse().getContentAsString(), IncomeGetDto.class);

        //then
        assertEquals(incomeGetDto.currency(), incomeGetDto.mainCurrency());
        assertEquals(incomeGetDto.amount(), incomeGetDto.amountInMainCurrency());
        mockMvc.perform(get(getRequestUrl(incomeGetDto.id())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(incomeGetDto.id()))
                .andExpect(jsonPath("$.title").value(incomeGetDto.title()))
                .andExpect(jsonPath("$.amount").value(incomeGetDto.amount()))
                .andExpect(jsonPath("$.amountInMainCurrency").value(incomeGetDto.amountInMainCurrency()))
                .andExpect(jsonPath("$.currency").value(incomeGetDto.currency().toString()))
                .andExpect(jsonPath("$.mainCurrency").value(incomeGetDto.mainCurrency().toString()))
                .andExpect(jsonPath("$.category.id").value(incomeGetDto.category().id()))
                .andExpect(jsonPath("$.category.title").value(incomeGetDto.category().title()))
                .andExpect(jsonPath("$.category.type").value(incomeGetDto.category().type().toString()))
                .andReturn();

        //clean up
        mockMvc.perform(delete(getRequestUrl(incomeGetDto.id())))
                .andExpect(status().isOk());
    }

    @Test
    void createIncome_whenIncomeDtoWithOtherCurrency_thenCreatesIncome() throws Exception {
        //when
        MvcResult result = mockMvc
                .perform(post(INCOMES_API_URL)
                        .content(mapper.writeValueAsString(new IncomeCreateDto("Gift", "Wednesday gift", LocalDate.now(), 5.0, Currency.EUR, 3)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        var incomeGetDto = mapper.readValue(result.getResponse().getContentAsString(), IncomeGetDto.class);

        //then
        assertNotEquals(incomeGetDto.currency(), incomeGetDto.mainCurrency());
        assertNotEquals(incomeGetDto.amount(), incomeGetDto.amountInMainCurrency());
        assertEquals(incomeGetDto.amount() * incomeGetDto.rate(), incomeGetDto.amountInMainCurrency(), 0.001);
        mockMvc.perform(get(getRequestUrl(incomeGetDto.id())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(incomeGetDto.id()))
                .andExpect(jsonPath("$.title").value(incomeGetDto.title()))
                .andExpect(jsonPath("$.amount").value(incomeGetDto.amount()))
                .andExpect(jsonPath("$.amountInMainCurrency").value(incomeGetDto.amountInMainCurrency()))
                .andExpect(jsonPath("$.currency").value(incomeGetDto.currency().toString()))
                .andExpect(jsonPath("$.mainCurrency").value(incomeGetDto.mainCurrency().toString()))
                .andExpect(jsonPath("$.category.id").value(incomeGetDto.category().id()))
                .andExpect(jsonPath("$.category.title").value(incomeGetDto.category().title()))
                .andExpect(jsonPath("$.category.type").value(incomeGetDto.category().type().toString()))
                .andReturn();

        //clean up
        mockMvc.perform(delete(getRequestUrl(incomeGetDto.id())))
                .andExpect(status().isOk());
    }


    @Test
    void updateIncome_whenIncomeIdIsPresentAndDtoIsOk_thenUpdatesExistingIncome() throws Exception {
        //given - creating new category
        MvcResult result = mockMvc
                .perform(post(INCOMES_API_URL)
                        .content(mapper.writeValueAsString(new IncomeCreateDto("Gift", "Wednesday gift", LocalDate.now(), 5.0, Currency.EUR, 3)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        var incomeGetDto = mapper.readValue(result.getResponse().getContentAsString(), IncomeGetDto.class);

        //when
        MvcResult resultUpdated = mockMvc.perform(put(getRequestUrl(incomeGetDto.id()))
                        .content(mapper.writeValueAsString(new IncomeCreateDto("Gift", "Wednesday gift", LocalDate.now(), 7.0, Currency.EUR, 3)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        var incomeGetDtoUpdated = mapper.readValue(resultUpdated.getResponse().getContentAsString(), IncomeGetDto.class);

        //then
        assertNotEquals(incomeGetDtoUpdated.currency(), incomeGetDtoUpdated.mainCurrency());
        assertNotEquals(incomeGetDtoUpdated.amount(), incomeGetDtoUpdated.amountInMainCurrency());
        assertEquals(incomeGetDtoUpdated.amount() * incomeGetDtoUpdated.rate(), incomeGetDtoUpdated.amountInMainCurrency(), 0.001);
        mockMvc.perform(get(getRequestUrl(incomeGetDto.id())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(incomeGetDtoUpdated.id()))
                .andExpect(jsonPath("$.title").value(incomeGetDtoUpdated.title()))
                .andExpect(jsonPath("$.amount").value("7.0"))
                .andExpect(jsonPath("$.amountInMainCurrency").value(incomeGetDtoUpdated.amountInMainCurrency()))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andExpect(jsonPath("$.mainCurrency").value(incomeGetDtoUpdated.mainCurrency().toString()))
                .andExpect(jsonPath("$.category.id").value(incomeGetDtoUpdated.category().id()))
                .andExpect(jsonPath("$.category.title").value(incomeGetDtoUpdated.category().title()))
                .andExpect(jsonPath("$.category.type").value(incomeGetDtoUpdated.category().type().toString()))
                .andReturn();

        //clean up
        mockMvc.perform(delete(getRequestUrl(incomeGetDtoUpdated.id())))
                .andExpect(status().isOk());
    }

    @Test
    void updateIncome_whenIncomeIdIsNotPresentAndDtoIsOk_thenReturnsNotFoundError() throws Exception {
        mockMvc.perform(put(getRequestUrl(107))
                        .content(mapper.writeValueAsString(new IncomeCreateDto("Wine", "Wednesday wine", LocalDate.now(), 5.0, Currency.EUR, 2)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteIncome_whenIncomeIdIsNotPresent_thenReturnsNotFoundError() throws Exception {
        mockMvc.perform(delete(getRequestUrl(107)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteIncome_whenIncomeIdIsPresent_thenDeletesIncome() throws Exception {
        //given - creating new expense
        MvcResult result = mockMvc
                .perform(post(INCOMES_API_URL)
                        .content(mapper.writeValueAsString(new IncomeCreateDto("Gift", "Wednesday gift", LocalDate.now(), 5.0, Currency.EUR, 3)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        var incomeGetDto = mapper.readValue(result.getResponse().getContentAsString(), IncomeGetDto.class);

        //when
        mockMvc.perform(delete(getRequestUrl(incomeGetDto.id())))
                .andExpect(status().isOk());

        //then
        mockMvc.perform(get(getRequestUrl(incomeGetDto.id())))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    private String getRequestUrl(long pathVariable) {
        return INCOMES_API_URL + "/" + pathVariable;
    }
}
