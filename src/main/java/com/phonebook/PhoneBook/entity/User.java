package com.phonebook.PhoneBook.entity;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.phonebook.PhoneBook.util.LoginStatus;

import org.hibernate.validator.constraints.Range;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "phonebook_user")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
    @NotBlank(message = "username cannot be empty")
    private String username;

    @JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
    @NotBlank(message = "email cant be empty")
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
    private String countryCode = "+91";

    @JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
    @Column(name = "phone_number", nullable=false)
    @Range(min = 99999999, max= (long)1e12)
    private long phoneNumber;

    @JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
    @JsonIgnoreProperties(allowGetters = false, allowSetters = true)
    private String password;

    @JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
    private LoginStatus status = LoginStatus.LOGGED_OUT;
    
    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private Otp otp;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Contacts> contacts;

    @JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
    private Timestamp createdOn = new Timestamp(System.currentTimeMillis());

}
