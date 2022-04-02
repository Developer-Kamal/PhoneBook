package com.phonebook.PhoneBook.repo;

import java.util.List;

import javax.transaction.Transactional;

import com.phonebook.PhoneBook.entity.Contacts;
import com.phonebook.PhoneBook.entity.User;
import com.phonebook.PhoneBook.util.ContactStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ContactsRepository extends JpaRepository<Contacts, Long>{
    
    public List<Contacts> findByUser(User user);

    @Modifying
    @Transactional
    @Query(value = "update user_contacts set status=?1 where id=?2", nativeQuery= true)
    public void updateContactStatus(ContactStatus status, long id);

    public List<Contacts> findByUserAndStatus(User user, ContactStatus status);
}
