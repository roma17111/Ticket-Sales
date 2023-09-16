package ru.service.ticketsales.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.service.ticketsales.dto.CarrierDto;
import ru.service.ticketsales.models.Carrier;

@Mapper(componentModel = "spring")
public interface CarrierMapper {

    CarrierDto toDto(Carrier carrier);
    Carrier toCarrier(CarrierDto carrierDto);
}
