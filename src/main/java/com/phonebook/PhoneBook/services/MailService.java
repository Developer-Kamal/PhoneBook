package com.phonebook.PhoneBook.services;

import com.phonebook.PhoneBook.util.Mail;

public interface MailService {
    public void sendEmail(Mail<?> mail);
}
