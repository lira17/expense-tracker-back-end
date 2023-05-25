package com.lira17.expensetracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lira17.expensetracker.common.ExpenseCategoryType;
import com.lira17.expensetracker.dto.create.ExpenseCategoryCreateDto;
import com.lira17.expensetracker.dto.get.ExpenseCategoryGetDto;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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
public class ExpenseCategoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    private static final String EXPENSE_CATEGORIES_API_URL = "/api/expense-categories";

    @Test
    void getAllCategoriesRequest_whenGet_thenReturnsDtoWithCategories() throws Exception {
        mockMvc.perform(get(EXPENSE_CATEGORIES_API_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[0].id").value("1"))
                .andExpect(jsonPath("$.[0].title").value("Rent"))
                .andExpect(jsonPath("$.[0].type").value("ESSENTIAL"))
                .andExpect(jsonPath("$.[1].id").value("2"))
                .andExpect(jsonPath("$.[1].title").value("Concerts"))
                .andExpect(jsonPath("$.[1].type").value("FUN"))
                .andExpect(jsonPath("$.[2].id").value("3"))
                .andExpect(jsonPath("$.[2].title").value("Food"))
                .andExpect(jsonPath("$.[2].type").value("ESSENTIAL"))
                .andExpect(jsonPath("$.[3].id").value("4"))
                .andExpect(jsonPath("$.[3].title").value("Travel"))
                .andExpect(jsonPath("$.[3].type").value("FUN"))
                .andExpect(jsonPath("$.[4].id").value("5"))
                .andExpect(jsonPath("$.[4].title").value("Transport"))
                .andExpect(jsonPath("$.[4].type").value("ESSENTIAL"))
                .andReturn();
    }


    @Test
    void getCategoryById_whenIdIsPresent_thenReturnsDtoWithCategory() throws Exception {
        mockMvc.perform(get(getRequestUrl(1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("Rent"))
                .andExpect(jsonPath("$.type").value("ESSENTIAL"))
                .andReturn();
    }

    @Ignore
    @Test
    void getCategoryById_whenIdIsNotFound_thenReturnsNotFoundError() throws Exception {
        mockMvc.perform(get(getRequestUrl(100)))
                .andExpect(status().isNotFound())
                .andReturn();
    }


    @Test
    void createCategory_whenCategoryDtoIsOk_thenCreatesCategory() throws Exception {
        //when
        MvcResult result = mockMvc
                .perform(post(EXPENSE_CATEGORIES_API_URL)
                        .content(mapper.writeValueAsString(new ExpenseCategoryCreateDto("Bills", ExpenseCategoryType.ESSENTIAL)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        var expenseCategoryGetDto = mapper.readValue(result.getResponse().getContentAsString(), ExpenseCategoryGetDto.class);

        //then
        mockMvc.perform(get(getRequestUrl(expenseCategoryGetDto.id())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(expenseCategoryGetDto.id()))
                .andExpect(jsonPath("$.title").value(expenseCategoryGetDto.title()))
                .andExpect(jsonPath("$.type").value(expenseCategoryGetDto.type().toString()))
                .andReturn();
    }

    @Test
    void updateCategory_whenCategoryIdIsPresentAndDtoIsOk_thenUpdatesExistingCategory() throws Exception {
        //given - creating new category
        MvcResult result = mockMvc
                .perform(post(EXPENSE_CATEGORIES_API_URL)
                        .content(mapper.writeValueAsString(new ExpenseCategoryCreateDto("Health", ExpenseCategoryType.ESSENTIAL)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        var expenseCategoryGetDto = mapper.readValue(result.getResponse().getContentAsString(), ExpenseCategoryGetDto.class);

        //when
        MvcResult resultUpdated = mockMvc.perform(put(getRequestUrl(expenseCategoryGetDto.id()))
                        .content(mapper.writeValueAsString(new ExpenseCategoryCreateDto("Health and Fitness", ExpenseCategoryType.ESSENTIAL)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        var expenseCategoryGetDtoUpdated = mapper.readValue(resultUpdated.getResponse().getContentAsString(), ExpenseCategoryGetDto.class);

        //then
        mockMvc.perform(get(getRequestUrl(expenseCategoryGetDto.id())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(expenseCategoryGetDtoUpdated.id()))
                .andExpect(jsonPath("$.title").value(expenseCategoryGetDtoUpdated.title()))
                .andExpect(jsonPath("$.type").value(expenseCategoryGetDtoUpdated.type().toString()))
                .andReturn();
    }

    @Test
    void updateCategory_whenCategoryIdIsNotPresentAndDtoIsOk_thenReturnsNotFoundError() throws Exception {
        mockMvc.perform(put(getRequestUrl(107))
                        .content(mapper.writeValueAsString(new ExpenseCategoryCreateDto("Books", ExpenseCategoryType.ESSENTIAL)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCategory_whenCategoryIdIsNotPresent_thenReturnsNotFoundError() throws Exception {
        mockMvc.perform(delete(getRequestUrl(107)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCategory_whenCategoryIdIsPresent_thenDeletesCategory() throws Exception {
        //given - creating new category
        MvcResult result = mockMvc
                .perform(post(EXPENSE_CATEGORIES_API_URL)
                        .content(mapper.writeValueAsString(new ExpenseCategoryCreateDto("Books", ExpenseCategoryType.ESSENTIAL)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        var expenseCategoryGetDto = mapper.readValue(result.getResponse().getContentAsString(), ExpenseCategoryGetDto.class);

        //when
        mockMvc.perform(delete(getRequestUrl(expenseCategoryGetDto.id())))
                .andExpect(status().isOk());

        //then
        mockMvc.perform(get(getRequestUrl(expenseCategoryGetDto.id())))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    private String getRequestUrl(long pathVariable) {
        return EXPENSE_CATEGORIES_API_URL + "/" + pathVariable;
    }
}
