package ru.service.ticketsales.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import ru.service.ticketsales.models.UserBuyer;
import ru.service.ticketsales.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserBuyer getByLogin(String login) {
        return userRepository.findByLogin(login);
    }
}
