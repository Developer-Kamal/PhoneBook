package com.phonebook.PhoneBook.util.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse<T> {

    private int code = 400;

    private int httpStatus = 400;

    private String message = "Bad Request";

    @JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
    private T errors;

    @Data
    public static class ErrorDetails {
        private String fieldName;
        private String message;
    }
}
