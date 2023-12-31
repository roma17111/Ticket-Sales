package ru.service.ticketsales.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TicketDto {

    long ticketId;
    RouteDto route;
    LocalDateTime departureDate;
    String seatNumber;
    long price;
    @JsonIgnore
    UserBuyerDto user;
    LocalDateTime buyDate;
}
