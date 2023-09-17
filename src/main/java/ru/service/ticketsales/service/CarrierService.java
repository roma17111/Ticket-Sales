package ru.service.ticketsales.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.RouteMatcher;
import ru.service.ticketsales.models.Carrier;
import ru.service.ticketsales.models.Route;
import ru.service.ticketsales.models.Ticket;
import ru.service.ticketsales.repository.CarrierRepository;
import ru.service.ticketsales.repository.RouteRepository;
import ru.service.ticketsales.repository.TicketRepository;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarrierService {

    private final CarrierRepository carrierRepository;
    private final TicketRepository ticketRepository;
    private final RouteRepository routeRepository;

    public void deleteByCarrierId(long carrierId) {
        Carrier carrier = carrierRepository.findById(carrierId);
        List<Route> routes = routeRepository.findAllByCarrierId(carrierId);
        if (routes == null || routes.size() == 0) {
            if (carrier != null) {
                carrierRepository.deleteById(carrierId);
                return;
            }
        } else {
            routes.forEach(route -> {
                List<Ticket> tickets = ticketRepository.findAllByRouteId(route.getRouteId());
                System.out.println(tickets);
                if (tickets == null || tickets.size() == 0) {
                    routeRepository.deleteById(route.getRouteId());
                } else {
                    tickets.forEach(ticket -> {
                        ticketRepository.deleteById(ticket.getTicketId());
                        routeRepository.deleteById(route.getRouteId());
                    });
                }
            });
            carrierRepository.deleteById(carrierId);
        }
    }
}
