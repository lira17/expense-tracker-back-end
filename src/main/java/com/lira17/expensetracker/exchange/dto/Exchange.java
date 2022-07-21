package com.lira17.expensetracker.exchange.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Exchange(String date, String historical, Info info, Query query, double result,
                       boolean success) {
}
