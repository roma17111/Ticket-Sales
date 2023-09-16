package ru.service.ticketsales.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.service.ticketsales.models.Carrier;
import ru.service.ticketsales.models.Ticket;
import ru.service.ticketsales.repository.CarrierRepository;
import ru.service.ticketsales.repository.TicketRepository;
import ru.service.ticketsales.repository.UserRepository;
import ru.service.ticketsales.mappers.UserMapper;
import ru.service.ticketsales.models.UserBuyer;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    private final TicketRepository ticketRepository;
    private final CarrierRepository carrierRepository;

    private final UserMapper userMapper;



}
