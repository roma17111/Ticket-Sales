package ru.service.ticketsales.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CarrierAlreadyExistsException extends Exception {

    public CarrierAlreadyExistsException(String message) {
        super(message);
    }
}
