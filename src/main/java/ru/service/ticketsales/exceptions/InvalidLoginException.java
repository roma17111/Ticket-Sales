package ru.service.ticketsales.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidLoginException extends Exception{

    public InvalidLoginException(String message) {
        super(message);
    }
}
