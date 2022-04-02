package com.phonebook.PhoneBook.repo;

import com.phonebook.PhoneBook.entity.Otp;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    
}
