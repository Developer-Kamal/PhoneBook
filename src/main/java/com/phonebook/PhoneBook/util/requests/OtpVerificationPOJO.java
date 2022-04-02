package com.phonebook.PhoneBook.util.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpVerificationPOJO {
    String CountryCode;
    long phoneNo;
    String otp;
}
