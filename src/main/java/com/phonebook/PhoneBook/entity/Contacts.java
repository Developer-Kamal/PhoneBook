package com.phonebook.PhoneBook.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.phonebook.PhoneBook.util.ContactStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table( name = "user_contacts")
@Entity
public class Contacts {
   
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
    private String name;

    @JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
    private String email;

    @JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
    private String countryCode;

    @JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
    private Long phoneNo;

    @JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
    private String img;

    @JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
    private String disc;

    @JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
    private ContactStatus status = ContactStatus.REGULAR;

    @JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
    private Timestamp createdTime = new Timestamp(System.currentTimeMillis());

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

}
