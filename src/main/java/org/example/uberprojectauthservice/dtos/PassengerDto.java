package org.example.uberprojectauthservice.dtos;

import lombok.*;
import org.example.uberprojectentityservice.models.Passenger;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassengerDto {

    private String id;

    private String name;

    private String email;

    private String password;           // encrypted password

    private String phoneNumber;

    private Date createdAt;

    public static PassengerDto from(Passenger p) {
        PassengerDto result = PassengerDto.builder()
                .id(p.getId().toString())
                .name(p.getName())
                .email(p.getEmail())
                .phoneNumber(p.getPhoneNumber())
                .password(p.getPassword())
                .createdAt(p.getCreatedAt())
                .build();

        return result;
    }
}
