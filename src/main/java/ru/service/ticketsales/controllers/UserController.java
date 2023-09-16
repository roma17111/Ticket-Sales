package ru.service.ticketsales.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.service.ticketsales.UserRepository;
import ru.service.ticketsales.dto.UserBuyerDto;
import ru.service.ticketsales.mappers.UserMapper;
import ru.service.ticketsales.models.UserBuyer;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;


    private  final UserMapper userMapper;

    @PostMapping("add")
    public ResponseEntity<UserBuyerDto> save(UserBuyer user) {
        userRepository.save(user);
        return ResponseEntity.ok(userMapper.toDto(user));
    }
}
