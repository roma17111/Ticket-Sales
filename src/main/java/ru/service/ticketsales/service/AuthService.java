package ru.service.ticketsales.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.service.ticketsales.dto.RegisterDto;
import ru.service.ticketsales.models.UserBuyer;
import ru.service.ticketsales.repository.UserRepository;
import ru.service.ticketsales.security.Role;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public void register(RegisterDto register) {
        PasswordEncoder encoder = new BCryptPasswordEncoder(12);
        UserBuyer buyer = UserBuyer.builder()
                .login(register.getLogin())
                .password(encoder.encode(register.getPassword()))
                .firstName(register.getFirstName())
                .lastName(register.getLastName())
                .secondName(register.getSecondName())
                .build();
        long userId = userRepository.createUser(buyer);
        userRepository.addRole(Role.BUYER, userId);
    }

    public List<UserBuyer> findAll() {
        return userRepository.findAll();
    }
}
