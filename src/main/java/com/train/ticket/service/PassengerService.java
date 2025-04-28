package com.train.ticket.service;

import com.train.ticket.entity.Passenger;
import com.train.ticket.repo.PassengerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PassengerService {

    private final PassengerRepository repository;

    public PassengerService(PassengerRepository repository, EmailService emailService){
        this.repository = repository;
    }

    public List<Passenger> allPassengers(){
        List<Passenger> passengers = new ArrayList<>();
        repository.findAll().forEach(passengers :: add);
        return  passengers;
    }
}
