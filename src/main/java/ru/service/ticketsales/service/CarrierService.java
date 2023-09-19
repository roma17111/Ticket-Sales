package ru.service.ticketsales.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.service.ticketsales.dto.CarrierDto;
import ru.service.ticketsales.dto.NewCarrierDto;
import ru.service.ticketsales.exceptions.CarrierAlreadyExistsException;
import ru.service.ticketsales.exceptions.CarrierNotFoundException;
import ru.service.ticketsales.mappers.CarrierMapper;
import ru.service.ticketsales.models.Carrier;
import ru.service.ticketsales.models.Route;
import ru.service.ticketsales.models.Ticket;
import ru.service.ticketsales.repository.CarrierRepository;
import ru.service.ticketsales.repository.RouteRepository;
import ru.service.ticketsales.repository.TicketRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarrierService {

    private final CarrierRepository carrierRepository;
    private final TicketRepository ticketRepository;
    private final RouteRepository routeRepository;
    private final CarrierMapper carrierMapper;


    public void createCarrier(NewCarrierDto carrierDto) throws CarrierAlreadyExistsException {
        CarrierDto dto = CarrierDto.builder()
                .carrierName(carrierDto.getName())
                .phoneNumber(carrierDto.getPhoneNumber())
                .build();
        Carrier carrierMap = carrierMapper.toCarrier(dto);
        Carrier carrier = carrierRepository.findByName(dto.getCarrierName());
        if (carrier != null && carrier.equals(carrierMap)) {
            throw new CarrierAlreadyExistsException("Такой перевозчик уже существует!");
        } else {
            carrierRepository.createCarrier(carrierMap);
        }
    }

    public void deleteByCarrierId(long carrierId) throws CarrierNotFoundException {
        Carrier carrier = carrierRepository.findById(carrierId);
        if (carrier == null) {
            throw new CarrierNotFoundException("Перевозчик не найден!");
        }
        List<Route> routes = routeRepository.findAllByCarrierId(carrierId);
        if (routes == null || routes.size() == 0) {
            carrierRepository.deleteById(carrierId);
        } else {
            routes.forEach(route -> {
                List<Ticket> tickets = ticketRepository.findAllByRouteId(route.getRouteId());
                if (tickets == null || tickets.size() == 0) {
                    routeRepository.deleteById(route.getRouteId());
                } else {
                    tickets.forEach(ticket -> {
                        ticketRepository.deleteByRouteId(route.getRouteId());
                        routeRepository.deleteById(route.getRouteId());
                    });
                }
            });
            carrierRepository.deleteById(carrierId);
        }
    }

    public CarrierDto updateCarrier(CarrierDto carrier) throws CarrierNotFoundException {
        if (carrierRepository.findById(carrier.getCarrierId()) == null) {
            throw new CarrierNotFoundException("Перевозчик не найден!");
        } else {
            carrierRepository.updateCarrier(carrierMapper.toCarrier(carrier));
        }
        return carrier;
    }

    public List<CarrierDto> findAll(long page) {
        List<CarrierDto> crs = new ArrayList<>();
        carrierRepository.findAll(page).forEach(
                el -> crs.add(carrierMapper.toDto(el))
        );
        return crs;
    }

}
