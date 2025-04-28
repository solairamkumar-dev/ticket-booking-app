package com.train.ticket.repo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.train.ticket.entity.Passenger;

@Repository
public interface PassengerRepository extends CrudRepository<Passenger, Long> {
    //Optional<Passenger> findByEmail(String email);
    Optional<Passenger> findByUsername(String username);
    Optional<Passenger> findByVerificationCode(String verificationCode);
}
