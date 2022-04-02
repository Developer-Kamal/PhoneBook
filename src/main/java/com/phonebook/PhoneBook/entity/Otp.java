package com.phonebook.PhoneBook.entity;

import java.sql.Timestamp;
import java.util.Random;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table( name = "user_otp")
public class Otp {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
    private int otp = 100000 + Math.abs((new Random().nextInt() % 899999));

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id" ,referencedColumnName = "id", nullable = false)
    private User user;

    @JsonInclude(content = Include.NON_NULL, value= Include.NON_NULL)
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    @JsonInclude(content = Include.NON_NULL, value = Include.NON_NULL)
    private Timestamp expTime = new Timestamp(System.currentTimeMillis()+ (10*60*1000));

}
