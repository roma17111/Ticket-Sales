package ru.service.ticketsales.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.service.ticketsales.models.Ticket;
import ru.service.ticketsales.repository.TicketRepository;
import ru.service.ticketsales.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final AuthService authService;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public List<Ticket> findAllByDate(LocalDateTime dateTime,
                                      long page) {
        return ticketRepository.findAllTicketsByDateDeparture(dateTime, page);
    }

    public List<Ticket> findAllByDeparture(String departure,
                                           long page) {
        return ticketRepository.findAllTicketsByRouteDeparture(departure, page);
    }

    public List<Ticket> findAllByDestination(String destination,
                                             long page) {
        return ticketRepository.findAllTicketsByDestination(destination, page);
    }

    public List<Ticket> getAllByFullFilter(LocalDateTime dateDeparture,
                                           String destination,
                                           String departure,
                                           long page) {
        return ticketRepository.findAllByFullFilter(dateDeparture,
                destination, departure, page);
    }

    public List<Ticket> getAllByDeparturePointAndDestination(String departure,
                                                        String destination,
                                                        long page) {
        return ticketRepository.findAllByDepartureAndDestination(departure, destination, page);
    }
    public List<Ticket> getAllByDepartureDateAndDestination(LocalDateTime departureDate,
                                                        String destination,
                                                        long page) {
        return ticketRepository.findAllByDepartureDateAndDestinationPoint(departureDate, destination, page);
    }
    public List<Ticket> getAllByDepartureDateAndDeparturePoint(LocalDateTime departureDate,
                                                            String departure,
                                                            long page) {
        return ticketRepository.findAllByDepartureDateAndDeparturePoint(departureDate, departure, page);
    }


}
