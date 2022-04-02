package com.phonebook.PhoneBook.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import com.phonebook.PhoneBook.entity.Contacts;
import com.phonebook.PhoneBook.entity.Otp;
import com.phonebook.PhoneBook.entity.User;
import com.phonebook.PhoneBook.repo.ContactsRepository;
import com.phonebook.PhoneBook.repo.OtpRepository;
import com.phonebook.PhoneBook.repo.UserRepository;
import com.phonebook.PhoneBook.util.ContactStatus;
import com.phonebook.PhoneBook.util.JwtUtil;
import com.phonebook.PhoneBook.util.LoginStatus;
import com.phonebook.PhoneBook.util.Mail;

import org.hibernate.MappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService implements UserDetailsService{
    
    @Autowired
    private UserRepository userRepository;

    @Autowired 
    private OtpRepository otpRepository;

    @Autowired
    private ContactsRepository contactsRepository;

    @Autowired
    private JwtUtil jwtUtil;


    public User save(User user){
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        Otp otp = new Otp();
        User userSaved = userRepository.save(user);
        otp.setUser(userSaved);
        otpRepository.save(otp);
        return userSaved;
    }

    public Otp sendOtp(String countryCode, Long phoneNo) throws MappingException, IOException, MessagingException{
        List<User> _user = userRepository.findByPhoneNumberAndCountryCode(phoneNo, countryCode);
        if(_user.isEmpty()){
            return null;
        }
        User user = _user.get(0);
        Otp newOtp = new Otp();
        user.getOtp().setOtp(newOtp.getOtp());
        Otp otp = (user.getOtp()==null)?newOtp:user.getOtp();
        otp.setUser(user);
        Otp savedOtp = otpRepository.save(otp);

        //Send mail
        Mail<?> mail = new Mail<>();
        mail.setMailFrom("ikkakuosb8@gmail.com");
        mail.setMailTo(user.getEmail());
        mail.setMailSubject("Otp for Registration");
        mail.setMailContent("here is otp: "+otp.getOtp());
        return savedOtp;

    }

    public User getOne(String id){
        User user = userRepository.getById(Long.parseLong(id));
        return user;
    }

    public User getOneByEmail(String email){
        User user = userRepository.findByEmail(email);
        return user;
    }


    public String verifyOtp(long phoneNo, String countryCode, int otp){
        List<User> user = userRepository.findByPhoneNumberAndCountryCode(phoneNo, countryCode);
        return (!user.isEmpty() && user.get(0).getOtp().getOtp() == otp)?jwtUtil.generateTokenFronString(String.valueOf(user.get(0).getEmail())):null;
    }

    public Contacts saveContacts(String email, Contacts contacts){
        User user = userRepository.findByEmail(email);
        contacts.setUser(user);
        Contacts contactSaved = contactsRepository.save(contacts);
        return contactSaved;
    }

    public Contacts editContact(long id, Contacts contacts){
        Contacts _contact = contactsRepository.getById(id);
        _contact.setCountryCode(contacts.getCountryCode());
        _contact.setDisc(contacts.getDisc());
        _contact.setEmail(contacts.getEmail());
        _contact.setName(contacts.getName());
        _contact.setPhoneNo(contacts.getPhoneNo());
        _contact.setImg(contacts.getImg());
    
        Contacts updatedContact = contactsRepository.save(_contact);
        return updatedContact;
    }

    public List<Contacts> getMyContacts(String email){
        User user = userRepository.findByEmail(email);
        List<Contacts> contacts = contactsRepository.findByUser(user);
        return contacts;
    }

    public void deleteContact(String email, long id){
        Contacts contact = contactsRepository.getById(id);
        contactsRepository.delete(contact);
    }

    public void updateStatus(String email,LoginStatus status){
        userRepository.updateUserStatus(status, email);
    }

    public void updateStatusByPhoneNo(String countryCode, long phoneNo, LoginStatus status){
        userRepository.findByPhoneAndUpdate(status, phoneNo, countryCode);
    }

    public void updateContactStatus(long id, ContactStatus status){
        contactsRepository.updateContactStatus(status, id);
    }

    public ArrayList<Contacts> getUserContactsByStatus(User user, ContactStatus status){
        ArrayList<Contacts> contacts = (ArrayList<Contacts>) contactsRepository.findByUserAndStatus(user, status);
        return contacts;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("before User email: "+email);
        User user = userRepository.findByEmail(email);
        log.info("After User =>");
        return new org.springframework.security.core.userdetails.User(email, user.getPassword(), new ArrayList<>());
    }

    
    public String getUserName(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = null;
        if(principal instanceof UserDetails){
            email = ((UserDetails)principal).getUsername();
        }else{
            email = principal.toString();
        }
        return email;
    }

}
