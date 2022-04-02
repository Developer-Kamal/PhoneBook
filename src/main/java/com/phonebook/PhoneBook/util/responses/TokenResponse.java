package com.phonebook.PhoneBook.util.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse{
       int code = 200;
       int httpCode = 200;
       String message = "authentication token is generated!";
       String token;

       public TokenResponse(String token){
           this.token = token;
       }
}
