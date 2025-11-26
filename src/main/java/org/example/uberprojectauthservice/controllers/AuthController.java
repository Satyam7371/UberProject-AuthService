package org.example.uberprojectauthservice.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.example.uberprojectauthservice.dtos.AuthRequestDto;
import org.example.uberprojectauthservice.dtos.AuthResponseDto;
import org.example.uberprojectauthservice.dtos.PassengerDto;
import org.example.uberprojectauthservice.dtos.PassengerSignupRequestDto;
import org.example.uberprojectauthservice.services.AuthService;
import org.example.uberprojectauthservice.services.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Value("${cookie.expiry}")
    private int cookieExpiry;

    private final AuthService authService;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup/passenger")
    public ResponseEntity<PassengerDto> signUp(@RequestBody PassengerSignupRequestDto passengerSignupRequestDto) {
        PassengerDto response = authService.signupPassenger(passengerSignupRequestDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/signin/passenger")
    public ResponseEntity<?> signIn(@RequestBody AuthRequestDto authRequestDto, HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(), authRequestDto.getPassword()));


        if (authentication.isAuthenticated()) {
            String jwtToken = jwtService.createToken(authRequestDto.getEmail());
            ResponseCookie cookie = ResponseCookie.from("JwtToken", jwtToken)      // response cookie inorder to send the token as HttpOnly cookie
                                    .httpOnly(true)
                                    .secure(false)
                                    .path("/")
                                    .maxAge(cookieExpiry)
                                    .build();

            response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());        // header is directly set without returning it

            return new ResponseEntity<>(AuthResponseDto.builder().success(true).build(), HttpStatus.OK);
        }
        else  {
            return new ResponseEntity<>("Auth unsuccessful", HttpStatus.OK);
        }
    }
}
