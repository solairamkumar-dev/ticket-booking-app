package com.train.ticket.service;

import com.train.ticket.dto.LoginPassengerDto;
import com.train.ticket.dto.RegisterPassengerDto;
import com.train.ticket.dto.VerifyPassengerDto;
import com.train.ticket.entity.Passenger;
import com.train.ticket.repo.PassengerRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthenticationService {

    private final PassengerRepository passengerRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final EmailService emailService;

    public AuthenticationService(
            PassengerRepository passengerRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            EmailService emailService){
        this.passengerRepository = passengerRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager  = authenticationManager;
        this.emailService = emailService;
    }


    public Passenger signUp(RegisterPassengerDto dto){
        if(checkForDuplicateEmail(dto.getUsername())){
            throw new RuntimeException("Account already exists!");
        }
        Passenger passenger = new Passenger(dto.getUsername(), passwordEncoder.encode(dto.getPassword()));
        passenger.setVerificationCode(generateVerificationCode());
        passenger.setVerificationCodeExpirationAt(LocalDateTime.now().plusMinutes(15));
        passenger.setEnabled(true);
        sendVerificationEmail(passenger);
        return passengerRepository.save(passenger);
    }


    public Passenger authenticate(LoginPassengerDto dto){
        Passenger passenger = passengerRepository.findByUsername(dto.getUsername()).orElseThrow(
                ()->new RuntimeException("Passenger not found"));

        if(!passenger.isEnabled()){
            throw new RuntimeException("Account not verified! please verify your account!");
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.getUsername(),
                            dto.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException("Invalid credentials");
        }
        return passenger;
   }

   public void verifyPassenger(VerifyPassengerDto dto){
       Optional<Passenger> optionalPassenger = passengerRepository.findByUsername(dto.getUsername());
       if(optionalPassenger.isPresent()){
           Passenger passenger = optionalPassenger.get();
           if(passenger.getVerificationCodeExpirationAt().isBefore(LocalDateTime.now())){
               throw new RuntimeException("verification code is expired");
           }
           if(passenger.getVerificationCode().equals(dto.getVerificationCode())){
               passenger.setEnabled(true);
               passenger.setVerificationCode(null);
               passenger.setVerificationCodeExpirationAt(null);
               passengerRepository.save(passenger);
           } else {
               throw new RuntimeException("Invalid verification code");
           }
       } else {
           throw new RuntimeException("passenger not found");
       }
   }

   public void resendVerificationCode(String username){
        Optional<Passenger> optionalPassenger = passengerRepository.findByUsername(username);

        if(optionalPassenger.isPresent()){
            Passenger passenger = optionalPassenger.get();
            if(passenger.isEnabled()){
                throw new RuntimeException("Account is verified already!");
            }

            passenger.setVerificationCode(generateVerificationCode());
            passenger.setVerificationCodeExpirationAt(LocalDateTime.now().plusMinutes(15));
            sendVerificationEmail(passenger);
            passengerRepository.save(passenger);
        } else {
            throw new RuntimeException("Passenger not found");
        }
   }

    private void sendVerificationEmail(Passenger passenger) {
        String subject = "Account Verification";
        String verificationCode = passenger.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
        try{
            emailService.sendVerificationEmail(passenger.getUsername(), subject, htmlMessage);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000)*100000;
        return String.valueOf(code);
    }

    private boolean checkForDuplicateEmail(String username) {
        return passengerRepository.findByUsername(username).isPresent();
    }

}
