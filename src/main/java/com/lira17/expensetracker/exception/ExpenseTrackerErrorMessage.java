package com.lira17.expensetracker.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class ExpenseTrackerErrorMessage {
    private String message;
    private int httpStatusCode;
    private HttpStatus httpStatus;
    private String uri;
}
