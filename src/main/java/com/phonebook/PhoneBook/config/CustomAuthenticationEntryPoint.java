package com.phonebook.PhoneBook.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.phonebook.PhoneBook.util.Utility;
import com.phonebook.PhoneBook.util.Constants;
import com.phonebook.PhoneBook.util.responses.ErrorResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component("restAuthenticationEntryPoint")
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint{

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        log.info("entry point: "+authException.getLocalizedMessage()+" "+response.getStatus()+" "+response.getOutputStream().toString());
        ErrorResponse<?> errorResponse = new ErrorResponse<>();
        errorResponse.setCode(403);
        errorResponse.setHttpStatus(403);
        errorResponse.setMessage(Constants.INVALID_TOKEN);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().println(new Utility().convertObjectToJson(errorResponse) );
        response.getOutputStream().close();
   
    }

    
}
