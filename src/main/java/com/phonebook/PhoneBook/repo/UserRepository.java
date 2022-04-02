package com.phonebook.PhoneBook.repo;

import java.util.List;

import javax.transaction.Transactional;

import com.phonebook.PhoneBook.entity.User;
import com.phonebook.PhoneBook.util.LoginStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    
    // @Query(value = "select * from phonebook_user where phone_number=?1 and country_code='?2'")
    public List<User> findByPhoneNumberAndCountryCode(long phoneNo, String countryCode);

    public User findByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "update phonebook_user set status=?1 where email=?2", nativeQuery = true)
    public void updateUserStatus(LoginStatus status, String email);

    @Modifying
    @Transactional
    @Query(value = "update phonebook_user set status=?1 where phone_number=?2 and country_code='?3'", nativeQuery = true)
    public void findByPhoneAndUpdate(LoginStatus status,long phoneNo, String countryCode);
    
}
