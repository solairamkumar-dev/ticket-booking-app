package com.train.ticket.controller;

import com.train.ticket.dto.LoginPassengerDto;
import com.train.ticket.dto.RegisterPassengerDto;
import com.train.ticket.dto.VerifyPassengerDto;
import com.train.ticket.entity.Passenger;
import com.train.ticket.responses.LoginResponse;
import com.train.ticket.service.AuthenticationService;
import com.train.ticket.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(
            JwtService jwtService,
            AuthenticationService authenticationService){
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }


    @PostMapping("/signup")
    public ResponseEntity<Passenger> register(@RequestBody RegisterPassengerDto registerPassengerDto) {
        Passenger registerPassenger = authenticationService.signUp(registerPassengerDto);
        return ResponseEntity.ok(registerPassenger);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginPassengerDto loginPassengerDto) {
        Passenger authenticatedPassenger = authenticationService.authenticate(loginPassengerDto);
        String jwtToken = jwtService.generateToken(authenticatedPassenger);
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyPassengerDto verifyPassengerDto) {
        try {
            authenticationService.verifyPassenger(verifyPassengerDto);
            return  ResponseEntity.ok("Account Verified Successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerification(@RequestParam String email){
        try {
            authenticationService.resendVerificationCode(email);
            return  ResponseEntity.ok("Verification code sent!");
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
