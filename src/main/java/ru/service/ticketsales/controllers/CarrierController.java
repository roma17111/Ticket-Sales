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
import ru.service.ticketsales.dto.CarrierDto;
import ru.service.ticketsales.dto.ErrorResponseDto;
import ru.service.ticketsales.dto.NewCarrierDto;
import ru.service.ticketsales.exceptions.CarrierAlreadyExistsException;
import ru.service.ticketsales.exceptions.CarrierNotFoundException;
import ru.service.ticketsales.service.CarrierService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carriers")
@RequiredArgsConstructor
public class CarrierController {

    private final CarrierService carrierService;

    @PostMapping("/add")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Добавить нового перевозчика")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Перевозчик успешно добавлен"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа!!!"),
            @ApiResponse(responseCode = "400", description = "Такой перевозчик уже есть в базе!")
    })
    public ResponseEntity<?> createCarrier(@RequestBody NewCarrierDto carrierDto) {
        try {
            carrierService.createCarrier(carrierDto);
        } catch (CarrierAlreadyExistsException e) {
            return ResponseEntity.status(400).body(ErrorResponseDto.builder()
                    .codeStatus(400)
                    .message(e.getMessage())
                    .build());
        }
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/update")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Отредактировать перевозчика в БД")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа!!!"),
            @ApiResponse(responseCode = "400", description = "Перевозчик не найден!")
    })
    public ResponseEntity<?> update(@RequestBody CarrierDto carrierDto) {
        CarrierDto newCarrier = null;
        try {
            newCarrier = carrierService.updateCarrier(carrierDto);
        } catch (CarrierNotFoundException e) {
            return ResponseEntity
                    .status(400)
                    .body(ErrorResponseDto.builder()
                    .codeStatus(400)
                    .message(e.getMessage())
                    .build());
        }
        return ResponseEntity.ok(newCarrier);
    }

    @DeleteMapping("/delete")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Удалить перевозчика из базы данных",
            description = "Данная операция может повлечь за собой каскадные удаления" +
                    "взаимосвязанных объектов из других таблиц БД"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошёл успешно"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён!!!"),
            @ApiResponse(responseCode = "400", description = "Перевозчик не найден!!!")
    })
    public ResponseEntity<?> deleteCarrierById(@RequestParam long carrierId) {
        try {
            carrierService.deleteByCarrierId(carrierId);
        } catch (CarrierNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponseDto.builder()
                    .codeStatus(400)
                    .message(e.getMessage())
                    .build());
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all{page}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Список перевозчиков в БД")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ок"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён!!!")
    })
    public ResponseEntity<List<CarrierDto>> findAll(@PathVariable long page) {
        return ResponseEntity.ok(carrierService.findAll(page));
    }
}
