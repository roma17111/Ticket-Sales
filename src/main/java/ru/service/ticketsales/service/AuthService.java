package ru.service.ticketsales.service;

import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.service.ticketsales.dto.RegisterDto;
import ru.service.ticketsales.exceptions.InvalidLoginException;
import ru.service.ticketsales.models.UserBuyer;
import ru.service.ticketsales.repository.UserRepository;
import ru.service.ticketsales.security.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;
    private final PasswordEncoder encoder;

    public boolean register(RegisterDto register) throws InvalidLoginException {
        if (userRepository.findByLogin(register.getLogin())!=null) {
            throw new InvalidLoginException("Логин уже занят!!!");
        }
        UserBuyer buyer = UserBuyer.builder()
                .login(register.getLogin())
                .password(encoder.encode(register.getPassword()))
                .firstName(register.getFirstName())
                .lastName(register.getLastName())
                .secondName(register.getSecondName())
                .build();
        long userId = userRepository.createUser(buyer);
        userRepository.addRole(Role.BUYER, userId);
        return true;
    }

    public List<UserBuyer> findAll() {
        return userRepository.findAll();
    }

    public JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException {
        final UserBuyer user = userService.getByLogin(authRequest.getLogin());
        if (user == null) {
            throw new AuthException("Пользователь " + authRequest.getLogin() + " не найден");
        }
        if (encoder.matches(authRequest.getPassword(),user.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.getLogin(), refreshToken);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new AuthException("Неправильный пароль");
        }
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final UserBuyer user = userService.getByLogin(login);
                if (user == null) {
                    throw new AuthException("Пользователь  не найден");
                }
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }

    public JwtResponse refresh(@NonNull String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final UserBuyer user = userService.getByLogin(login);
                if (user == null) {
                    throw new AuthException("Пользователь  не найден");
                }
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getLogin(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Невалидный JWT токен");
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }
}
