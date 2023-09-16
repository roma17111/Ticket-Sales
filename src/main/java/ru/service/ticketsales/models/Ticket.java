package ru.service.ticketsales.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Ticket {

    long ticketId;
    Route route;
    LocalDateTime departureDate;
    String seatNumber;
    long price;
    UserBuyer user;
    LocalDateTime buyDate;
}
