package ru.service.ticketsales.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TicketIsBuyedException extends Exception{

    public TicketIsBuyedException(String message) {
        super(message);
    }
}
