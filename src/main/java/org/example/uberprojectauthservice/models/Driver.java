package org.example.uberprojectauthservice.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler" , "bookings"})
public class Driver extends BaseModel {

    private String name;

    @Column(nullable = false, unique = true)
    private String licenseNumber;

    private String phoneNumber;


    @OneToMany(mappedBy = "driver")          // it is basically used in composition like when one is attached to another and data is fetched then if it is eager by default lazy then join will be created on the associated table as well
    @Fetch(FetchMode.SUBSELECT)                   // this is to solve n+1 problem by executing only 2 queries
    private List<Booking> bookings;
}
