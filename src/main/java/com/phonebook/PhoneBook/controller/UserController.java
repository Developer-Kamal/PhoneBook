package com.phonebook.PhoneBook.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.mail.MessagingException;

import com.phonebook.PhoneBook.entity.Contacts;
import com.phonebook.PhoneBook.entity.Otp;
import com.phonebook.PhoneBook.entity.User;
import com.phonebook.PhoneBook.services.UserService;
import com.phonebook.PhoneBook.util.Constants;
import com.phonebook.PhoneBook.util.ContactStatus;
import com.phonebook.PhoneBook.util.JwtUtil;
import com.phonebook.PhoneBook.util.LoginStatus;
import com.phonebook.PhoneBook.util.requests.LoginBody;
import com.phonebook.PhoneBook.util.requests.OtpVerificationPOJO;
import com.phonebook.PhoneBook.util.responses.Response;
import com.phonebook.PhoneBook.util.responses.TokenResponse;

import org.hibernate.MappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody User user){
        User savedUser = userService.save(user);
        return ResponseEntity.ok(new Response<User>(201, 201, "Registration Sucessfull!", savedUser));
    }

    @PostMapping("send-otp")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> sendOtp(@RequestBody OtpVerificationPOJO otpBody) throws MappingException, IOException, MessagingException{
        Otp sendOtp = userService.sendOtp(otpBody.getCountryCode(), otpBody.getPhoneNo());
        return ResponseEntity.ok(new Response<Otp>(200, 200, "Otp send Sucessfully", sendOtp));
    }

    @GetMapping("{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> getOne(@PathVariable String id){
        User user = userService.getOne(id);
        return ResponseEntity.ok(new Response<User>(200, 200, "User fetched Sucessfully!", user));
    }

    @PostMapping("verify-otp")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> verify(@RequestBody OtpVerificationPOJO otpBody){
        String token = userService.verifyOtp(otpBody.getPhoneNo(), otpBody.getCountryCode(), Integer.parseInt(otpBody.getOtp()));
        if(token != null)  return ResponseEntity.ok(new TokenResponse(token));
        userService.updateStatusByPhoneNo(otpBody.getCountryCode(), otpBody.getPhoneNo(), LoginStatus.LOGGED_IN);
        return ResponseEntity.ok(new TokenResponse(400, 400, "otp did not match", null));
    }

    @PostMapping("login")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> login(@RequestBody LoginBody loginBody) throws Exception{
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginBody.getEmail(), loginBody.getPassword()));
            final UserDetails userDetails = userService.loadUserByUsername(loginBody.getEmail());
            final String token = jwtUtil.generateToken(userDetails);
            userService.updateStatus(loginBody.getEmail(), LoginStatus.LOGGED_IN);
            return ResponseEntity.ok(new TokenResponse(token));
        } catch (Exception e) {
            log.info("in exception "+e.getLocalizedMessage());
            e.printStackTrace();
            throw new Exception("Incorrect email or password", e);
        }
        
    }

    @PostMapping("add-contact")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> addContact(@RequestBody Contacts contacts){
        String email = userService.getUserName();
        Contacts savedContacts = userService.saveContacts(email, contacts);
        return ResponseEntity.ok(new Response<>(201, 201, "contact added sucessfully", savedContacts));
    }

    @PostMapping("edit-contacts")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> editContact(@RequestBody Contacts contacts){
        long id = contacts.getId();
        Contacts editedContact = userService.editContact(id, contacts);
        return ResponseEntity.ok(new Response<>(200, 200, "contact edited sucessfully!", editedContact));
    }

    @GetMapping("me")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> myProfile(){
        String email = userService.getUserName();
        System.out.println(email);
        User user = userService.getOneByEmail(email);
        return ResponseEntity.ok(new Response<>(200, 200, "profile fetched sucessfully!", user));
    }

    @DeleteMapping("delete")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> deleteMyAccount(){
        String email = userService.getUserName();
        userService.updateStatus(email, LoginStatus.DELETED);
        return ResponseEntity.ok(new Response<>(200, 200, "account deleted!",null));
    }

    @GetMapping("logout")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> logout(){
        String email=userService.getUserName();
        userService.updateStatus(email, LoginStatus.LOGGED_OUT);
        return ResponseEntity.ok(new Response<>(200, 200, "logout Sucessfully!", null));
    }


    @GetMapping("my-contacts")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> myContacts(){
        String email = userService.getUserName();
        ArrayList<Contacts> contacts =(ArrayList<Contacts>) userService.getMyContacts(email);
        return ResponseEntity.ok(new Response<>(200, 200, "contacts fetched sucessfully!", contacts));
    }

    @DeleteMapping("contact/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> deleteContact(@PathVariable String id){
        userService.updateContactStatus(Long.parseLong(id), ContactStatus.DELETED);
        return ResponseEntity.ok(new Response<>(200, 200, Constants.SUCCESS, null));
    }

    @PutMapping("contact/block/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> blockContact(@PathVariable String id){
        userService.updateContactStatus(Long.parseLong(id), ContactStatus.BLOCKED);
        return ResponseEntity.ok(new Response<>(200, 200, Constants.SUCCESS, null));
    }

    @PutMapping("contact/favourate/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> favourateContact(@PathVariable String id){
        userService.updateContactStatus(Long.parseLong(id), ContactStatus.FAVOURATE);
        return ResponseEntity.ok(new Response<>(200, 200, Constants.SUCCESS, null));
    }

    @PutMapping("contact/regular/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> regularContact(@PathVariable String id){
        userService.updateContactStatus(Long.parseLong(id), ContactStatus.REGULAR);
        return ResponseEntity.ok(new Response<>(200, 200, Constants.SUCCESS, null));
    }
    @GetMapping("contact/blocked")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> blockedContacts(){
        String email = userService.getUserName();
        User user = userService.getOneByEmail(email);
        ArrayList<Contacts> contacts = userService.getUserContactsByStatus(user, ContactStatus.BLOCKED);
        return ResponseEntity.ok(new Response<>(200, 200, Constants.SUCCESS, contacts));
    }

    @GetMapping("contact/favourate")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> favourateContacts(){
        String email = userService.getUserName();
        User user = userService.getOneByEmail(email);
        ArrayList<Contacts> contacts = userService.getUserContactsByStatus(user, ContactStatus.FAVOURATE);
        return ResponseEntity.ok(new Response<>(200, 200, Constants.SUCCESS, contacts));
    }

    @RequestMapping("hello")
    public String hello(){
        return "Welcome to phonebook";
    }


}
