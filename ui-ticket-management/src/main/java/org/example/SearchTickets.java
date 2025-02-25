package org.example;

import org.example.Ticket;

import java.util.List;
import java.util.stream.Collectors;

public class SearchTickets {
    public static List<Ticket> searchById(List<Ticket> tickets, int ticketId) {
        return tickets.stream()
                .filter(ticket -> ticket.getId()== ticketId)
                .collect(Collectors.toList());
    }

    public static List<Ticket> searchByStatus(List<Ticket> tickets, String status) {
        return tickets.stream()
                .filter(ticket -> ticket.getId().equals(status))
                .collect(Collectors.toList());
    }
}
