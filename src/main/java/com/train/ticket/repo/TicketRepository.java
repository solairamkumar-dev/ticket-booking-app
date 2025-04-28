package com.train.ticket.repo;

import com.train.ticket.dto.TicketDto;
import com.train.ticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

}
