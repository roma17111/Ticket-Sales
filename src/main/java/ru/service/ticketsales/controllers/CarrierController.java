package ru.service.ticketsales.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.service.ticketsales.dto.ErrorResponseDto;
import ru.service.ticketsales.exceptions.CarrierNotFoundException;
import ru.service.ticketsales.service.CarrierService;

@RestController
@RequestMapping("/api/v1/carriers")
@RequiredArgsConstructor
public class CarrierController {

    private final CarrierService carrierService;

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
}
