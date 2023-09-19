package ru.service.ticketsales.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RouteNotFoundException extends Exception{

    public RouteNotFoundException(String message) {
        super(message);
    }
}
