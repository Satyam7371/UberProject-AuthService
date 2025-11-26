package org.example.uberprojectauthservice.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestDto {

    private String email;

    private String password;
}
