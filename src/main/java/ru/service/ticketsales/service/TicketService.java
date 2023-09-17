package ru.service.ticketsales.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.service.ticketsales.dto.TicketDto;
import ru.service.ticketsales.exceptions.TicketIsBuyedException;
import ru.service.ticketsales.mappers.TicketMapper;
import ru.service.ticketsales.models.Ticket;
import ru.service.ticketsales.models.UserBuyer;
import ru.service.ticketsales.repository.TicketRepository;
import ru.service.ticketsales.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final AuthService authService;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final TicketMapper ticketMapper;


    public void buyTicket(long ticketId) throws TicketIsBuyedException {
        String login = authService.getAuthInfo().getUsername();
        UserBuyer user = userRepository.findByLogin(login);
        long userId = user.getUserId();
        Ticket ticket = ticketRepository.findById(ticketId);
        if (ticket.getUser() != null) {
            throw new TicketIsBuyedException("Билет уже куплен!!!");
        }
        ticketRepository.buyTicket(userId,ticketId);
    }

    public List<TicketDto> getUserTickets() {
        String login = authService.getAuthInfo().getUsername();
        UserBuyer user = userRepository.findByLogin(login);
        long userId = user.getUserId();
        List<TicketDto> ticketDtos = new ArrayList<>();
        ticketRepository.getUserTickets(userId).forEach(el -> ticketDtos.add(ticketMapper.toDto(el)));
        return ticketDtos;
    }

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
