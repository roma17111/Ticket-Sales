package ru.service.ticketsales.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.service.ticketsales.dto.ErrorResponseDto;
import ru.service.ticketsales.dto.RegisterDto;
import ru.service.ticketsales.exceptions.InvalidLoginException;
import ru.service.ticketsales.security.JwtRequest;
import ru.service.ticketsales.security.JwtResponse;
import ru.service.ticketsales.security.RefreshJwtRequest;
import ru.service.ticketsales.service.AuthService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> register(@RequestBody RegisterDto dto) {
        try {
            authService.register(dto);
        } catch (InvalidLoginException e) {
            return ResponseEntity.status(400)
                    .body(ErrorResponseDto.builder()
                            .codeStatus(400)
                            .message(e.getMessage())
                            .build());
        }
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyAuthority('BUYER')")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/all")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(authService.findAll());
    }

    @PostMapping("login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) {
        final JwtResponse token;
        try {
            token = authService.login(authRequest);
        } catch (AuthException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(token);
    }

    @PostMapping("token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token;
        try {
            token = authService.getAccessToken(request.getRefreshToken());
        } catch (AuthException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(token);
    }

    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token;
        try {
            token = authService.refresh(request.getRefreshToken());
        } catch (AuthException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(token);
    }

}
