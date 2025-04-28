package com.train.ticket.controller;

import com.train.ticket.entity.Passenger;
import com.train.ticket.repo.PassengerRepository;
import com.train.ticket.service.PassengerService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/passengers")
public class PassengerController {

    private final PassengerService service;

    private final PassengerRepository repository;

    public PassengerController(PassengerService service, PassengerRepository repository){
        this.service = service;
        this.repository = repository;
    }

    @GetMapping("/me")
    public ResponseEntity<?> authenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null || !authentication.isAuthenticated()){
            return ResponseEntity.status(401).body("User is not authenticated");
        }
        Passenger currentPassenger = (Passenger) authentication.getPrincipal();
        return ResponseEntity.ok(currentPassenger);
    }

    @GetMapping("/")
    public ResponseEntity<List<Passenger>> allPassengers(){
        List<Passenger> passengers  = service.allPassengers();
        return  ResponseEntity.ok(passengers);
    }

}
