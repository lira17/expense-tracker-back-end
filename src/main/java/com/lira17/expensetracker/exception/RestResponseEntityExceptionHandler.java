package com.lira17.expensetracker.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleExceptions(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, null, new HttpHeaders(), getStatus(ex), request);
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
    
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception exception, Object body, HttpHeaders headers,
            HttpStatus status, WebRequest request
    ) {
        if (body == null) {
            body = getExceptionEntity(exception, request, status, exception.getMessage());
        }
        return super.handleExceptionInternal(exception, body, headers, status, request);
    }

    private HttpStatus getStatus(Throwable thr) {
        ResponseStatus annotation = thr.getClass().getAnnotation(ResponseStatus.class);
        return annotation == null ? INTERNAL_SERVER_ERROR : annotation.value();
    }

    private ExpenseTrackerErrorMessage getExceptionEntity(
            Exception exception,
            WebRequest request,
            HttpStatus status,
            String message
    ) {
        String errMessage = message != null ? message : exception.getMessage();
        logger.error(errMessage, exception);
        return new ExpenseTrackerErrorMessage(
                message,
                status.value(),
                status,
                ((ServletWebRequest) request).getRequest().getRequestURI()
        );
    }
}
