package com.example.pricing.prices.infrastructure.api;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.example.pricing.prices.application.exceptions.InvalidQueryException;
import com.example.pricing.prices.domain.exceptions.PriceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 400 – InvalidQueryException (use case preconditions)
    @ExceptionHandler(InvalidQueryException.class)
    public ProblemDetail handleInvalid(InvalidQueryException ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        pd.setTitle("Invalid parameters");
        return pd;
    }

    // 400 – Missing query param
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ProblemDetail handleMissingParam(MissingServletRequestParameterException ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Required request parameter '%s' is missing".formatted(ex.getParameterName()));
        pd.setTitle("Invalid parameters");
        return pd;
    }

    // 400 – Bad format (e.g., date not ISO-8601)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        String param = ex.getName();
        String value = String.valueOf(ex.getValue());
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Parameter '%s' with value '%s' has an invalid format".formatted(param, value));
        pd.setTitle("Invalid parameters");
        return pd;
    }

    // 400 – Bean Validation
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Invalid parameters");
        pd.setDetail("One or more parameters are invalid");
        pd.setProperty("violations", ex.getConstraintViolations().stream()
                .map((jakarta.validation.ConstraintViolation<?> v) -> Map.of(
                        "param", v.getPropertyPath().toString(),
                        "message", v.getMessage(),
                        "invalidValue", String.valueOf(v.getInvalidValue())))
                .toList());
        return pd;
    }

    // 404 – No applicable price
    @ExceptionHandler(PriceNotFoundException.class)
    public ProblemDetail handleNotFound(PriceNotFoundException ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Price not found");
        return pd;
    }

    // 500 – Fallback
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error");
        pd.setTitle("Internal error");
        return pd;
    }
}
