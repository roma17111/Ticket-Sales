package ru.service.ticketsales.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.service.ticketsales.models.Carrier;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NewRouteDto {

    long routeId;
    String departurePoint;
    String destination;
    NewCarrierDto carrier;
    long durationInMinutes;
}

