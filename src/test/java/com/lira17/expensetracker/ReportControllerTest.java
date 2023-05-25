package com.lira17.expensetracker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(initializers = {TestContainersInitializer.class})
public class ReportControllerTest {

    @Autowired
    MockMvc mockMvc;

    private static final String REPORT_API_URL = "/api/report";
    private static final String REPORT_MONTHS_API_URL = "/api/report/months";

    @Test
    void getReportRequest_whenReportForYearRequested_thenReturnsReportForYear() throws Exception {
        mockMvc.perform(get(REPORT_API_URL).queryParam("year", "2022"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.year").value("2022"))
                .andExpect(jsonPath("$.month").isEmpty())
                .andExpect(jsonPath("$.totalIncome").value("480000.0"))
                .andExpect(jsonPath("$.totalExpense").value("48000.0"))
                .andExpect(jsonPath("$.difference").value("432000.0"))
                .andExpect(jsonPath("$.balancePositive").value("true"))
                .andExpect(jsonPath("$.expenses.[0].categoryTotalAmount").value("12000.0"))
                .andExpect(jsonPath("$.expenses.[0].categoryPercent").value("25.0"))
                .andExpect(jsonPath("$.incomes.[0].categoryTotalAmount").isNumber())
                .andExpect(jsonPath("$.incomes.[0].categoryPercent").isNumber())
                .andReturn();
    }

    @Test
    void getReportRequest_whenReportForMonthRequested_thenReturnsReportForMonth() throws Exception {
        mockMvc.perform(get(REPORT_API_URL).queryParam("year", "2022").queryParam("month", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.year").value("2022"))
                .andExpect(jsonPath("$.month").value("1"))
                .andExpect(jsonPath("$.totalIncome").value("120000.0"))
                .andExpect(jsonPath("$.totalExpense").value("12000.0"))
                .andExpect(jsonPath("$.difference").value("108000.0"))
                .andExpect(jsonPath("$.balancePositive").value("true"))
                .andExpect(jsonPath("$.expenses.[0].categoryTotalAmount").value("12000.0"))
                .andExpect(jsonPath("$.expenses.[0].categoryPercent").value("100.0"))
                .andExpect(jsonPath("$.incomes.[0].categoryTotalAmount").value("120000.0"))
                .andExpect(jsonPath("$.incomes.[0].categoryPercent").value("100.0"))
                .andReturn();
    }

    @Test
    void getReportMonthlyRequest_whenReportForYearRequested_thenReturnsMonthlyReportForYear() throws Exception {
        mockMvc.perform(get(REPORT_MONTHS_API_URL).queryParam("year", "2022"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[0].year").value("2022"))
                .andExpect(jsonPath("$.[0].month").value("1"))
                .andExpect(jsonPath("$.[0].totalIncome").value("120000.0"))
                .andExpect(jsonPath("$.[0].totalExpense").value("12000.0"))
                .andExpect(jsonPath("$.[0].difference").value("108000.0"))
                .andExpect(jsonPath("$.[0].balancePositive").value("true"))
                .andExpect(jsonPath("$.[3].year").value("2022"))
                .andExpect(jsonPath("$.[3].month").value("4"))
                .andExpect(jsonPath("$.[3].totalIncome").value("120000.0"))
                .andExpect(jsonPath("$.[3].totalExpense").value("12000.0"))
                .andExpect(jsonPath("$.[3].difference").value("108000.0"))
                .andExpect(jsonPath("$.[3].balancePositive").value("true"))
                .andReturn();
    }
}
