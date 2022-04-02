package com.phonebook.PhoneBook.util.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {
    int code = 200;

    int httpCode = 200;

    String message = "Sucessfull";

    @JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
    T data;
}
