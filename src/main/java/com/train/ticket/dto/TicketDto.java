package com.train.ticket.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TicketDto {

    private String from;
    private String to;
    private int ticketPrice;
    private LocalDateTime ticketBookingTime;
    private String username;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTicketBarcodeText(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return String.format(
                "=========================\n" +
                        "      TRAIN TICKET      \n" +
                        "=========================\n" +
                        "Passenger : %s\n" +
                        "From      : %s\n" +
                        "To        : %s\n" +
                        "Price     : ₹%d\n" +
                        "Booked On : %s\n" +
                        "=========================",
                username, from, to, ticketPrice, ticketBookingTime.format(formatter));

    }
}
