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

    @GetMapping("test1")
    public ResponseEntity<UserBuyer> findBYLogin(@RequestParam String login) {
        return ResponseEntity.ok(userRepository.findByLogin(login));
    }

    @GetMapping("test2")
    public ResponseEntity<UserBuyer> findById(@RequestParam Long id) {
        return ResponseEntity.ok(userRepository.findById(id));
    }

    @PostMapping("/update")
    public ResponseEntity<Void> update(@RequestBody UserBuyer userBuyer) {
        userRepository.save(userBuyer);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteById(@RequestParam long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/createTicket")
    public ResponseEntity<Void> updateTicket(@RequestBody Ticket ticket) {
        ticketRepository.createTicket(ticket);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/createCarrier")
    public ResponseEntity<Void> updateCarrier(@RequestBody Carrier carrier) {
        carrierRepository.createCarrier(carrier);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/alltickets")
    public ResponseEntity<List<Ticket>> findAll(@RequestParam long numberPage) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(ticketRepository.findAll(numberPage));
    }

    @GetMapping("/all-tickets/{name}/{page}")
    public ResponseEntity<List<Ticket>> findAllByCareerName(@PathVariable String name,
                                                            @PathVariable long page) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(ticketRepository.getAllSaleTicketsByCareerName(name,page));
    }


}
