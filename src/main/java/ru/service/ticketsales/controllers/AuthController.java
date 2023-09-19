package ru.service.ticketsales.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.models.headers.Header;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.service.ticketsales.dto.ErrorResponseDto;
import ru.service.ticketsales.dto.RegisterDto;
import ru.service.ticketsales.dto.UserBuyerDto;
import ru.service.ticketsales.exceptions.InvalidLoginException;
import ru.service.ticketsales.security.JwtRequest;
import ru.service.ticketsales.security.JwtResponse;
import ru.service.ticketsales.security.RefreshJwtRequest;
import ru.service.ticketsales.service.AuthService;
import ru.service.ticketsales.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    @ResponseBody
    @Operation(summary = "Регистрация пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Вы успешно зарегистрировались"),
            @ApiResponse(responseCode = "400", description = "Логин уже занят")
    })
    public ResponseEntity<?> register(@RequestBody RegisterDto dto) {
        try {
            authService.register(dto);
        } catch (InvalidLoginException e) {
            log.error(e.getMessage(),e);
            return ResponseEntity.status(400)
                    .body(ErrorResponseDto.builder()
                            .codeStatus(400)
                            .message(e.getMessage())
                            .build());

        }
        return ResponseEntity.ok("Вы успешно зарегистрировались");
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/all")
    @Operation(summary = "Получение всех пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошёл успешно"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа")
    })
    public ResponseEntity<List<UserBuyerDto>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping("/login")
    @Operation(summary = "Авторизоваться",
            description = "Данные контроллер принимает refresh token и возвращает" +
                    " новуый access token"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошёл успешно"),
            @ApiResponse(responseCode = "400", description = "Ошибка авторизвции")
    })
    public ResponseEntity<?> login(@RequestBody JwtRequest authRequest) {
        final JwtResponse token;
        try {
            token = authService.login(authRequest);
        } catch (AuthException e) {
            log.error(e.getMessage(),e);
            return ResponseEntity.status(400).body(ErrorResponseDto.builder()
                    .codeStatus(400)
                    .message(e.getMessage())
                    .build());
        }
        return ResponseEntity.ok(token);
    }

    @PostMapping("/token")
    @Operation(summary = "Получить новый access токен",
            description = "Данные контроллер принимает refresh token и возвращает" +
                    " новуый access token"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошёл успешно"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token;
        try {
            token = authService.getAccessToken(request.getRefreshToken());
        } catch (AuthException e) {
            log.error(e.getMessage(),e);
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyAuthority('BUYER','ADMIN')")
    @Operation(summary = "Получить новую пару access and refresh токенов",
            description = "Данные контроллер принимает refresh token и возвращает новую пару токенов"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошёл успешно"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token;
        try {
            token = authService.refresh(request.getRefreshToken());
        } catch (AuthException e) {
            log.error(e.getMessage(),e);
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(token);
    }

}
