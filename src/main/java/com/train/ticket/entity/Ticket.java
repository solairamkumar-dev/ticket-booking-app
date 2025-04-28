package com.train.ticket.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name ="station_from")
    private String from;
    @Column(name="station_to")
    private String to;
    @Column(name="ticket_price")
    private int ticketPrice;
    @Column(name= "ticket_booking_time")
    private LocalDateTime ticketBookingTime;

    @ManyToOne
    @JoinColumn(name = "passenger_id", nullable = false)
    private Passenger passenger;

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(int ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public LocalDateTime getTicketBookingTime() {
        return ticketBookingTime;
    }

    public void setTicketBookingTime(LocalDateTime ticketBookingTime) {
        this.ticketBookingTime = ticketBookingTime;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public String getBarCodeText(){
        return id+ "-" + from + "-" + to + "-" + ticketPrice + "-" +ticketBookingTime+ passenger;
    }
}
