package ru.service.ticketsales.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.service.ticketsales.dto.TicketDto;
import ru.service.ticketsales.models.Ticket;

@Component
@Mapper(componentModel = "spring")
public interface TicketMapper {

    TicketDto toDto(Ticket ticket);
    Ticket toTicket(TicketDto ticketDto);
}
