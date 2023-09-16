package ru.service.ticketsales.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Route {

    long routeId;
    String departurePoint;
    String destination;
    Carrier carrier;
    long durationInMinutes;

}
