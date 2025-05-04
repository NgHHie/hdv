package com.example.service_customer.model;



import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

   
    @Column(name = "first_name", nullable = false)
    private String firstName;

   
    @Column(name = "last_name", nullable = false)
    private String lastName;

    
    @Column(unique = true, nullable = false)
    private String email;

   
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column
    private String address;


    @JsonIgnore
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Purchase> purchases;
}