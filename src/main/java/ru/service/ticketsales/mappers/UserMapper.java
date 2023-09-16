package ru.service.ticketsales.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.service.ticketsales.dto.UserBuyerDto;
import ru.service.ticketsales.models.UserBuyer;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserBuyerDto toDto(UserBuyer userBuyer);
    UserBuyer toUser(UserBuyerDto userBuyerDto);

}
