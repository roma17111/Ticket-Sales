package ru.service.ticketsales.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.service.ticketsales.repository.UserRepository;
import ru.service.ticketsales.mappers.UserMapper;
import ru.service.ticketsales.models.UserBuyer;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;


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

}
