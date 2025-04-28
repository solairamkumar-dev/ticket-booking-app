package com.train.ticket.controller;

import com.train.ticket.dto.TicketDto;
import com.train.ticket.entity.Ticket;
import com.train.ticket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketService service;

    @PostMapping("/booking")
    public ResponseEntity<?> bookTicket(@RequestBody TicketDto ticket){
       service.bookTicket(ticket);
      return ResponseEntity.ok("Ticket Sent to your email!");
    }
}
