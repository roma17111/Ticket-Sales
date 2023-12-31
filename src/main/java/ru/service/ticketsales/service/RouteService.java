package ru.service.ticketsales.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.service.ticketsales.dto.NewRouteDto;
import ru.service.ticketsales.dto.RouteDto;
import ru.service.ticketsales.exceptions.RouteNotFoundException;
import ru.service.ticketsales.mappers.RouteMapper;
import ru.service.ticketsales.models.Carrier;
import ru.service.ticketsales.models.Route;
import ru.service.ticketsales.models.Ticket;
import ru.service.ticketsales.repository.RouteRepository;
import ru.service.ticketsales.repository.TicketRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RouteService {

    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;
    private final TicketRepository ticketRepository;

    public void deleteRouteById(long routeId) throws RouteNotFoundException {
        Route route = routeRepository.findById(routeId);
        if (route == null) {
            throw new RouteNotFoundException("Маршрут не найден!!!");
        } else {
            List<Ticket> tickets = ticketRepository.findAllByRouteId(routeId);
            if (tickets == null || tickets.size() == 0) {
                routeRepository.deleteById(routeId);
            } else {
                tickets.forEach(
                        ticket -> {
                            ticketRepository.deleteByRouteId(routeId);
                        }
                );
            }
            routeRepository.deleteById(routeId);
            log.info("Маршрут удалён -" + route);
        }
    }

    public void createRoute(NewRouteDto routeDto) {
        Carrier carrier = Carrier.builder()
                .carrierName(routeDto.getCarrier().getName())
                .phoneNumber(routeDto.getCarrier().getPhoneNumber())
                .build();
        Route route = Route.builder()
                .carrier(carrier)
                .departurePoint(routeDto.getDeparturePoint())
                .destination(routeDto.getDestination())
                .durationInMinutes(routeDto.getDurationInMinutes())
                .build();
        routeRepository.save(route);
        log.info("Маршрут создан -" + route);
    }

    public void update(RouteDto route) throws RouteNotFoundException {
        if (routeRepository.findById(route.getRouteId()) == null) {
            throw new  RouteNotFoundException("Маршрут не найден");
        }
        routeRepository.updateRoute(routeMapper.toRoute(route));
        log.info("Маршрут изменён -" + route);
    }

    public List<RouteDto>   findAll(long page) {
        List<RouteDto> routeDtos = new ArrayList<>();
        routeRepository.findAll(page).forEach(
                el -> routeDtos.add(routeMapper.toDto(el))
        );
        return routeDtos;
    }
}
