package com.phonebook.PhoneBook.exception;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.phonebook.PhoneBook.util.Constants;
import com.phonebook.PhoneBook.util.responses.ErrorResponse;
import com.phonebook.PhoneBook.util.responses.ErrorResponse.ErrorDetails;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class CustomErrorResponses {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse<?> handleException(ConstraintViolationException ex) {

        Set<ConstraintViolation<?>> errors = ex.getConstraintViolations();
        log.error("Email or Password Incorrect "+ ex.getLocalizedMessage());
        Set<ErrorDetails> errorDetails = new HashSet<>();
        for (ConstraintViolation<?> fieldError : errors) {
            ErrorDetails error = new ErrorDetails();
            error.setFieldName(fieldError.getPropertyPath().toString());
            error.setMessage(fieldError.getMessage());
            errorDetails.add(error);
        }

        ErrorResponse<Set<ErrorDetails>> errorResponse = new ErrorResponse<>();
        errorResponse.setErrors(errorDetails);

        return errorResponse;
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponse<?> handleException(InternalAuthenticationServiceException ex) {
        String error = ex.getMessage();
        log.error("Email or Password Incorrect "+ error);
        return new ErrorResponse<>(401, 401, "Email or Password is incorrect!", null);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponse<?> handleException(BadCredentialsException ex) {
        String error = ex.getMessage();
        log.error("Email or Password Incorrect "+ error);
        return new ErrorResponse<>(401, 401, "Email or Password is incorrect!", null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse<?> handleException(DataIntegrityViolationException ex) {
        String error = ex.getMessage();
        log.error("Email already exists "+ error);
        String rootCause = ex.getRootCause().getMessage();
        String message = Constants.BAD_REQUEST;
        if(rootCause.contains("email") && rootCause.contains("exists")){
            message = Constants.EMAIL_EXISTS;
        }
        return new ErrorResponse<>(400, 400, message, null);
    }

}
