package com.train.ticket.service;


import com.train.ticket.constants.PathConstants;
import com.train.ticket.dto.TicketDto;
import com.train.ticket.entity.Passenger;
import com.train.ticket.entity.Ticket;
import com.train.ticket.repo.PassengerRepository;
import com.train.ticket.repo.TicketRepository;
import com.train.ticket.utils.QRCodeGenerator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class TicketService {


    private final PassengerRepository passengerRepository;
    private final TicketRepository ticketRepository;
    private final EmailService emailService;

    private String qrCodeFileName = "";

    public TicketService(PassengerRepository passengerRepository,
                         TicketRepository ticketRepository,
                         EmailService emailService) {
        this.passengerRepository=passengerRepository;
        this.ticketRepository=ticketRepository;
        this.emailService = emailService;
    }

    public void bookTicket(TicketDto dto){
        Passenger passenger = passengerRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Passenger not found"));
        Ticket ticket = new Ticket();
        ticket.setFrom(dto.getFrom());
        ticket.setTo(dto.getTo());
        ticket.setTicketPrice(dto.getTicketPrice());
        ticket.setTicketBookingTime(dto.getTicketBookingTime());
        ticket.setPassenger(passenger);
     try {
         ticketRepository.save(ticket);
          qrCodeFileName = QRCodeGenerator.generateQRCode(dto.getTicketBarcodeText(), 200, 200);
         ticketToEmail(dto);
     }catch (Exception e){
         throw new RuntimeException("Error while booking a ticket : "+e.getMessage());
     }
    }

    private void ticketToEmail(TicketDto dto){
        String mailSubject = "Ticket Details";
        String  htmlTicket = generateTicketHtml(dto);
        try {
            File qrFile = new File(PathConstants.qrCodeDirectory+qrCodeFileName);
            emailService.sendTicketToEmail(dto.getUsername(), mailSubject, htmlTicket, qrFile);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String generateTicketHtml(TicketDto dto) {
        return "<html>\n" +
                "<body style=\"font-family: Arial, sans-serif;\">\n" +
                "    <div style=\"max-width: 600px; margin: 0 auto; padding: 20px; border: 2px solid #333; border-radius: 10px;\">\n" +
                "        <div style=\"text-align: center; background-color: #007bff; color: white; padding: 10px; border-radius: 5px;\">\n" +
                "            <h2>Train Ticket</h2>\n" +
                "        </div>\n" +
                "        \n" +
                "        <div style=\"margin: 20px 0;\">\n" +
                "            <table style=\"width: 100%; border-collapse: collapse;\">\n" +
                "                <tr><td style=\"padding: 10px; border-bottom: 1px solid #ddd;\"><strong>From:</strong></td>\n" +
                "                    <td style=\"padding: 10px; border-bottom: 1px solid #ddd;\">" + dto.getFrom() + "</td></tr>\n" +
                "                <tr><td style=\"padding: 10px; border-bottom: 1px solid #ddd;\"><strong>To:</strong></td>\n" +
                "                    <td style=\"padding: 10px; border-bottom: 1px solid #ddd;\">" + dto.getTo() + "</td></tr>\n" +
                "                <tr><td style=\"padding: 10px; border-bottom: 1px solid #ddd;\"><strong>Price:</strong></td>\n" +
                "                    <td style=\"padding: 10px; border-bottom: 1px solid #ddd;\">&#8377; " + dto.getTicketPrice() + "</td></tr>\n" +
                "                <tr><td style=\"padding: 10px; border-bottom: 1px solid #ddd;\"><strong>Booking Time:</strong></td>\n" +
                "                    <td style=\"padding: 10px; border-bottom: 1px solid #ddd;\">" + dto.getTicketBookingTime() + "</td></tr>\n" +
                "                <tr><td style=\"padding: 10px; border-bottom: 1px solid #ddd;\"><strong>Passenger:</strong></td>\n" +
                "                    <td style=\"padding: 10px; border-bottom: 1px solid #ddd;\">" + dto.getUsername() + "</td></tr>\n" +
                "            </table>\n" +
                "        </div>\n" +
                "        \n" +
                "        <div style=\"text-align: center; margin-top: 20px; font-size: 12px; color: #666;\">\n" +
                "            <p>Thank you for choosing our service!</p>\n" +
                "            <p>Please show this ticket during your journey.</p>\n" +
                "        </div>\n" +
                "        \n" +
                "        <div style=\"text-align: center; margin-top: 20px;\">\n" +
                "            <p><strong>Scan QR Code:</strong></p>\n" +
                "            <img src=\"cid:qr-code\" alt=\"QR Code\" width=\"200\" height=\"200\">\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

}
