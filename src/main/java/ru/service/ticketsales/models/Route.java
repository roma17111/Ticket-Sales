package ru.service.ticketsales.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Route implements Serializable {

    long routeId;
    String departurePoint;
    String destination;
    Carrier carrier;
    long durationInMinutes;

}
