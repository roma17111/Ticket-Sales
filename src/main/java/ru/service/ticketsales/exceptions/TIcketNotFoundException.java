package ru.service.ticketsales.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TIcketNotFoundException extends Exception{

    public TIcketNotFoundException(String message) {
        super(message);
    }
}
