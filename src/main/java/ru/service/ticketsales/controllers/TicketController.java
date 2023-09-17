package ru.service.ticketsales.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.service.ticketsales.dto.ErrorResponseDto;
import ru.service.ticketsales.dto.TicketDto;
import ru.service.ticketsales.exceptions.TicketIsBuyedException;
import ru.service.ticketsales.models.Ticket;
import ru.service.ticketsales.repository.TicketRepository;
import ru.service.ticketsales.service.TicketService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketRepository ticketRepository;
    private final TicketService ticketService;

    @GetMapping("/all/{page}")
    public ResponseEntity<?> findAll(@PathVariable long page) {
        return ResponseEntity.ok(ticketRepository.findAllforSale(page));
    }

    @PostMapping("/buy")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('BUYER','ADMIN')")
    public ResponseEntity<?> buyTicket(@RequestParam long ticketId) {
        try {
            ticketService.buyTicket(ticketId);
            return ResponseEntity.ok().build();
        } catch (TicketIsBuyedException e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponseDto.builder()
                    .codeStatus(400)
                    .message(e.getMessage())
                    .build());
        }
    }


    @GetMapping("my")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('BUYER','ADMIN')")
    public ResponseEntity<List<TicketDto>> getUserTickets() {
        return ResponseEntity.ok(ticketService.getUserTickets());
    }


}
