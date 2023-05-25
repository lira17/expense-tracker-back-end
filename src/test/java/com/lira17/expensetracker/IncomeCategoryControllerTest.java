package com.lira17.expensetracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lira17.expensetracker.common.IncomeCategoryType;
import com.lira17.expensetracker.dto.create.IncomeCategoryCreateDto;
import com.lira17.expensetracker.dto.get.IncomeCategoryGetDto;
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
public class IncomeCategoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    private static final String INCOME_CATEGORIES_API_URL = "/api/income-categories";


    @Test
    void getAllCategoriesRequest_whenGet_thenReturnsDtoWithCategories() throws Exception {
        mockMvc.perform(get(INCOME_CATEGORIES_API_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[0].id").value("1"))
                .andExpect(jsonPath("$.[0].title").value("Salary"))
                .andExpect(jsonPath("$.[0].type").value("REGULAR"))
                .andExpect(jsonPath("$.[1].id").value("2"))
                .andExpect(jsonPath("$.[1].title").value("Investment"))
                .andExpect(jsonPath("$.[1].type").value("REGULAR"))
                .andExpect(jsonPath("$.[2].id").value("3"))
                .andExpect(jsonPath("$.[2].title").value("Gift"))
                .andExpect(jsonPath("$.[2].type").value("ONE_TIME"))
                .andReturn();
    }

    @Test
    void getCategoryById_whenIdIsPresent_thenReturnsDtoWithCategory() throws Exception {
        mockMvc.perform(get(getRequestUrl(1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("Salary"))
                .andExpect(jsonPath("$.type").value("REGULAR"))
                .andReturn();
    }

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
                .perform(post(INCOME_CATEGORIES_API_URL)
                        .content(mapper.writeValueAsString(new IncomeCategoryCreateDto("Bonus", IncomeCategoryType.ONE_TIME)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        var incomeCategoryGetDto = mapper.readValue(result.getResponse().getContentAsString(), IncomeCategoryGetDto.class);

        //then
        mockMvc.perform(get(getRequestUrl(incomeCategoryGetDto.id())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(incomeCategoryGetDto.id()))
                .andExpect(jsonPath("$.title").value(incomeCategoryGetDto.title()))
                .andExpect(jsonPath("$.type").value(incomeCategoryGetDto.type().toString()))
                .andReturn();
    }

    @Test
    void updateCategory_whenCategoryIdIsPresentAndDtoIsOk_thenUpdatesExistingCategory() throws Exception {
        //given - creating new category
        MvcResult result = mockMvc
                .perform(post(INCOME_CATEGORIES_API_URL)
                        .content(mapper.writeValueAsString(new IncomeCategoryCreateDto("Project bonus", IncomeCategoryType.ONE_TIME)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        var incomeCategoryGetDto = mapper.readValue(result.getResponse().getContentAsString(), IncomeCategoryGetDto.class);

        //when
        MvcResult resultUpdated = mockMvc.perform(put(getRequestUrl(incomeCategoryGetDto.id()))
                        .content(mapper.writeValueAsString(new IncomeCategoryCreateDto("Project bonus 2", IncomeCategoryType.ONE_TIME)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        var incomeCategoryGetDtoUpdated = mapper.readValue(resultUpdated.getResponse().getContentAsString(), IncomeCategoryGetDto.class);

        //then
        mockMvc.perform(get(getRequestUrl(incomeCategoryGetDto.id())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(incomeCategoryGetDtoUpdated.id()))
                .andExpect(jsonPath("$.title").value(incomeCategoryGetDtoUpdated.title()))
                .andExpect(jsonPath("$.type").value(incomeCategoryGetDtoUpdated.type().toString()))
                .andReturn();
    }

    @Test
    void updateCategory_whenCategoryIdIsNotPresentAndDtoIsOk_thenReturnsNotFoundError() throws Exception {
        mockMvc.perform(put(getRequestUrl(107))
                        .content(mapper.writeValueAsString(new IncomeCategoryCreateDto("Project bonus 2", IncomeCategoryType.ONE_TIME)))
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
                .perform(post(INCOME_CATEGORIES_API_URL)
                        .content(mapper.writeValueAsString(new IncomeCategoryCreateDto("Freelance", IncomeCategoryType.REGULAR)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        var incomeCategoryGetDto = mapper.readValue(result.getResponse().getContentAsString(), IncomeCategoryGetDto.class);

        //when
        mockMvc.perform(delete(getRequestUrl(incomeCategoryGetDto.id())))
                .andExpect(status().isOk());

        //then
        mockMvc.perform(get(getRequestUrl(incomeCategoryGetDto.id())))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    private String getRequestUrl(long pathVariable) {
        return INCOME_CATEGORIES_API_URL + "/" + pathVariable;
    }
}
