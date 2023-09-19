package ru.service.ticketsales.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.service.ticketsales.dto.ErrorResponseDto;
import ru.service.ticketsales.dto.NewRouteDto;
import ru.service.ticketsales.dto.RouteDto;
import ru.service.ticketsales.exceptions.RouteNotFoundException;
import ru.service.ticketsales.service.RouteService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/routes")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;


    @PostMapping("/add")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Добавить новоый маршрут")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Маршрут успешно добавлен"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа!!!")
    })
    public ResponseEntity<?> addRoute(@RequestBody NewRouteDto routeDto) {
        routeService.createRoute(routeDto);
        return ResponseEntity.ok().build();
    }


    @PatchMapping("/update")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Обновить маршрут")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Маршрут успешно изменён"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа!!!"),
            @ApiResponse(responseCode = "404", description = "Маршрут не найден.")
    })
    public ResponseEntity<?> updateRoute(@RequestBody RouteDto routeDto) {
        try {
            routeService.update(routeDto);
        } catch (RouteNotFoundException e) {
            return ResponseEntity.status(400).body(ErrorResponseDto.builder()
                    .codeStatus(400)
                    .message(e.getMessage())
                    .build());
        }
        return ResponseEntity.ok(routeDto);
    }


    @DeleteMapping("/delete")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Удалить маршрут из базы")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Маршрут успешно удашён"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа!!!"),
            @ApiResponse(responseCode = "404", description = "Маршрут не найден.")
    })
    public ResponseEntity<?> deleteRoute(@RequestParam long routeId) {
        try {
            routeService.deleteRouteById(routeId);
        } catch (RouteNotFoundException e) {
            return ResponseEntity.status(400).body(ErrorResponseDto.builder()
                    .codeStatus(400)
                    .message(e.getMessage())
                    .build());
        }
        return ResponseEntity.ok("Маршрут удалён");
    }

    @GetMapping("/all/{page}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Достать маршруты из базы", description = "Данный контроллер " +
            "позволяет постранично доставать маршруты из базы в количестве 20 шт на странице")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Маршрут успешно удашён"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа!!!"),
            @ApiResponse(responseCode = "404", description = "Маршрут не найден.")
    })
    public ResponseEntity<List<RouteDto>> getAll(@PathVariable long page) {
        return ResponseEntity.ok(routeService.findAll(page));
    }

}
