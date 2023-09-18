package ru.service.ticketsales.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import ru.service.ticketsales.dto.UserBuyerDto;
import ru.service.ticketsales.mappers.UserMapper;
import ru.service.ticketsales.models.UserBuyer;
import ru.service.ticketsales.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserBuyer getByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public List<UserBuyerDto> findAll() {
        List<UserBuyerDto> users = new ArrayList<>();
        userRepository.findAll().forEach(user -> users.add(userMapper.toDto(user)));
        return users;
    }
}
