package ru.service.ticketsales.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.service.ticketsales.dto.EditTicketDto;
import ru.service.ticketsales.dto.TicketDto;
import ru.service.ticketsales.exceptions.TIcketNotFoundException;
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
@Slf4j
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
        ticketRepository.buyTicket(userId, ticketId);
        log.info("Пользователь - " + user + " купил билет - " + ticket);
    }

    public List<TicketDto> findAllForSale(long page) {
        List<TicketDto> ticketDtos = new ArrayList<>();
        System.out.println(ticketRepository.findAllforSale(page));
        ticketRepository.findAllforSale(page).forEach(el -> ticketDtos.add(ticketMapper.toDto(el)));
        return ticketDtos;
    }

    public List<TicketDto> getUserTickets() {
        String login = authService.getAuthInfo().getUsername();
        UserBuyer user = userRepository.findByLogin(login);
        long userId = user.getUserId();
        List<TicketDto> ticketDtos = new ArrayList<>();
        ticketRepository.getUserTickets(userId).forEach(el -> ticketDtos.add(ticketMapper.toDto(el)));
        return ticketDtos;
    }

    public List<TicketDto> findAllByDate(LocalDateTime dateTime,
                                         long page) {
        List<TicketDto> ticketDtos = new ArrayList<>();
        ticketRepository.findAllTicketsByDateDeparture(dateTime, page).forEach(
                el -> ticketDtos.add(ticketMapper.toDto(el))
        );
        return ticketDtos;
    }

    public List<TicketDto> findAllByDeparture(String departure,
                                              long page) {
        List<TicketDto> ticketDtos = new ArrayList<>();
        ticketRepository.findAllTicketsByRouteDeparture(departure, page).forEach(
                el -> ticketDtos.add(ticketMapper.toDto(el))
        );
        return ticketDtos;
    }

    public List<TicketDto> findAllByDestination(String destination,
                                                long page) {
        List<TicketDto> ticketDtos = new ArrayList<>();
        ticketRepository.findAllTicketsByDestination(destination, page).forEach(
                el -> ticketDtos.add(ticketMapper.toDto(el))
        );
        return ticketDtos;
    }

    public List<TicketDto> getAllByFullFilter(LocalDateTime dateDeparture,
                                              String destination,
                                              String departure,
                                              long page) {
        List<TicketDto> ticketDtos = new ArrayList<>();
        ticketRepository.findAllByFullFilter(dateDeparture,
                destination, departure, page).forEach(
                el -> ticketDtos.add(ticketMapper.toDto(el))
        );
        return ticketDtos;
    }

    public List<TicketDto> getAllByDeparturePointAndDestination(String departure,
                                                                String destination,
                                                                long page) {
        List<TicketDto> ticketDtos = new ArrayList<>();
        ticketRepository.findAllByDepartureAndDestination(departure, destination, page).forEach(
                el -> ticketDtos.add(ticketMapper.toDto(el))
        );
        return ticketDtos;
    }

    public List<TicketDto> getAllByDepartureDateAndDestination(LocalDateTime departureDate,
                                                               String destination,
                                                               long page) {
        List<TicketDto> tickets = new ArrayList<>();
        ticketRepository.findAllByDepartureDateAndDestinationPoint(departureDate, destination, page).forEach(
                el -> tickets.add(ticketMapper.toDto(el))
        );
        return tickets;
    }

    public List<TicketDto> getAllByDepartureDateAndDeparturePoint(LocalDateTime departureDate,
                                                                  String departure,
                                                                  long page) {
        List<TicketDto> tickets = new ArrayList<>();
        ticketRepository.findAllByDepartureDateAndDeparturePoint(departureDate, departure, page).forEach(
                el -> tickets.add(ticketMapper.toDto(el))
        );
        return tickets;
    }

   public void update(EditTicketDto ticketDto) throws TIcketNotFoundException {
       if (ticketRepository.findById(ticketDto.getTicketId()) == null) {
           throw new TIcketNotFoundException("Билет не найден!");
       } else {
           Ticket ticket = Ticket.builder()
                   .price(ticketDto.getPrice())
                   .ticketId(ticketDto.getTicketId())
                   .seatNumber(ticketDto.getSeatNumber())
                   .departureDate(ticketDto.getDepartureDate())
                   .build();
           ticketRepository.update(ticket, ticketDto.getRouteId());
           log.info("Билет изменён - " + ticket);
       }
   }

   public void addTicket(TicketDto ticketDto) {
       ticketRepository.createTicket(ticketMapper.toTicket(ticketDto));
       log.info("Добавлен новый билет - " + ticketDto);
   }

   public void deleteTicketById(long ticketId) {
        ticketRepository.deleteById(ticketId);
   }
}
