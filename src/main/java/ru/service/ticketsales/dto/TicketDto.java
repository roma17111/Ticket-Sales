package ru.service.ticketsales.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.service.ticketsales.models.Route;
import ru.service.ticketsales.models.UserBuyer;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TicketDto {

    long ticket_id;
    RouteDto route;
    LocalDateTime departureDate;
    String seatNumber;
    long price;
    UserBuyerDto user;
    LocalDateTime buyDate;
}
