package ru.service.ticketsales.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.service.ticketsales.dto.RouteDto;
import ru.service.ticketsales.models.Route;

@Mapper(componentModel = "spring")
public interface RouteMapper {

    RouteDto toDto(Route route);
    Route toRoute(RouteDto routeDto);

}
