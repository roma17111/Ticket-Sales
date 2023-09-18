package ru.service.ticketsales.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketRepository ticketRepository;
    private final TicketService ticketService;

    @GetMapping("/all/{page}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('BUYER','ADMIN')")
    @Operation(summary = "Посмотреть билеты в продаже",
            description = "Данный endpoint позволяет постранично получать " +
                    "список имеющихся билетов в продаже в количестве 20 штук на странице" +
                    "в JSON формате"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошёл успешно"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён!!!")
    })
    public ResponseEntity<?> findAllForSale(@PathVariable long page) {
        return ResponseEntity.ok(ticketRepository.findAllforSale(page));
    }

    @PostMapping("/buy")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('BUYER','ADMIN')")
    @Operation(summary = "Купить билет")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Покупка билета прошла успешно"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён!!!"),
            @ApiResponse(responseCode = "400", description = "Билет уже куплен!")
    })
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


    @GetMapping("/my")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('BUYER','ADMIN')")
    @Operation(summary = "Посмотреть список купленных билетов авторизованного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошёл успешно"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён!!!")
    })
    public ResponseEntity<List<TicketDto>> getUserTickets() {
        return ResponseEntity.ok(ticketService.getUserTickets());
    }

    @GetMapping("/filter/date/{page}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('BUYER','ADMIN')")
    @Operation(summary = "Поиск билетов по дате",
            description = "Данный endpoint позволяет постранично получать " +
                    "список имеющихся билетов в продаже," +
                    "отфильтрованных по дате отправления, в количестве 20 штук на странице" +
                    "в JSON формате"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошёл успешно"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён!!!")
    })
    public ResponseEntity<List<Ticket>> filterByDepartureDate(@PathVariable long page,
                                                              @RequestParam LocalDateTime dateTime) {
        return ResponseEntity
                .ok(ticketRepository
                        .findAllTicketsByDateDeparture(dateTime, page));
    }

    @GetMapping("/filter/route/departure/{page}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('BUYER','ADMIN')")
    @Operation(summary = "Поиск билетов по пункту отправления",
            description = "Данный endpoint позволяет постранично получать " +
                    "список имеющихся билетов в продаже," +
                    "отфильтрованных по пункту отправления, в количестве 20 штук на странице" +
                    "в JSON формате"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошёл успешно"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён!!!")
    })
    public ResponseEntity<List<Ticket>> filterByDeparturePoint(@PathVariable long page,
                                                               @RequestParam String departure) {
        return ResponseEntity
                .ok(ticketRepository
                        .findAllTicketsByRouteDeparture(departure, page));
    }

    @GetMapping("/filter/route/destination/{page}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('BUYER','ADMIN')")
    @Operation(summary = "Поиск билетов по пункту назначения",
            description = "Данный endpoint позволяет постранично получать " +
                    "список имеющихся билетов в продаже," +
                    "отфильтрованных по пукту назначения, в количестве 20 штук на странице" +
                    "в JSON формате"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошёл успешно"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён!!!")
    })
    public ResponseEntity<List<Ticket>> filterByDestinationPoint(@PathVariable long page,
                                                                 @RequestParam String destination) {
        return ResponseEntity.ok(ticketRepository
                        .findAllTicketsByDestination(destination, page));
    }

    @GetMapping("/filter/{page}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('BUYER','ADMIN')")
    @Operation(summary = "Поиск билетов с полной фильтрацией посика",
            description = "Данный endpoint позволяет постранично получать " +
                    "список имеющихся билетов в продаже," +
                    "отфильтрованных по всем доступным фильтрам, в количестве 20 штук на странице" +
                    "в JSON формате"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошёл успешно"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён!!!")
    })
    public ResponseEntity<List<Ticket>> filterByAllParameters(@PathVariable long page,
                                                              @RequestParam LocalDateTime departureDate,
                                                              @RequestParam String departurePoint,
                                                              @RequestParam String destinationPoint) {
        return ResponseEntity
                .ok(ticketRepository
                        .findAllByFullFilter(departureDate,
                                destinationPoint,
                                departurePoint,
                                page));
    }

    @GetMapping("/filter/r/{page}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('BUYER','ADMIN')")
    @Operation(summary = "Поиск билетов по пункту отправления и пункту назначения",
            description = "Данный endpoint позволяет постранично получать " +
                    "список имеющихся билетов в продаже," +
                    "отфильтрованных по пункту отправления и пункту назначения," +
                    " в количестве 20 штук на странице" +
                    "в JSON формате"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошёл успешно"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён!!!")
    })
    public ResponseEntity<List<Ticket>> filterByFullRoute(@PathVariable long page,
                                                          @RequestParam String departurePoint,
                                                          @RequestParam String destinationPoint) {
        return ResponseEntity
                .ok(ticketRepository
                        .findAllByDepartureAndDestination(departurePoint, destinationPoint, page));
    }

    @GetMapping("/filter/dep/{page}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('BUYER','ADMIN')")
    @Operation(summary = "Поиск билетов по дате и пункту отправления",
            description = "Данный endpoint позволяет постранично получать " +
                    "список имеющихся билетов в продаже," +
                    "отфильтрованных по дате и пункту отправления, в количестве 20 штук на странице" +
                    "в JSON формате"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошёл успешно"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён!!!")
    })
    public ResponseEntity<List<Ticket>> filterByDepartureDateAndRoute(@PathVariable long page,
                                                                      @RequestParam LocalDateTime departureDate,
                                                                      @RequestParam String departurePoint) {
        return ResponseEntity
                .ok(ticketRepository
                        .findAllByDepartureDateAndDeparturePoint(departureDate,
                                departurePoint,
                                page));
    }

    @GetMapping("/filter/des/{page}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('BUYER','ADMIN')")
    @Operation(summary = "Поиск билетов по дате отправления и пункту назначения",
            description = "Данный endpoint позволяет постранично получать " +
                    "список имеющихся билетов в продаже," +
                    "отфильтрованных по дате отправления и пункту назначения," +
                    " в количестве 20 штук на странице" +
                    "в JSON формате"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошёл успешно"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён!!!")
    })
    public ResponseEntity<List<Ticket>> filterByDepartureDateAndDestinationRoute(@PathVariable long page,
                                                                                 @RequestParam LocalDateTime departureDate,
                                                                                 @RequestParam String destinationPoint) {
        return ResponseEntity
                .ok(ticketRepository
                        .findAllByDepartureDateAndDestinationPoint(departureDate,
                                destinationPoint,
                                page));
    }
}

