package ru.service.ticketsales.repository.rowmappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.service.ticketsales.models.Route;
import ru.service.ticketsales.models.Ticket;
import ru.service.ticketsales.models.UserBuyer;
import ru.service.ticketsales.repository.RouteRepository;
import ru.service.ticketsales.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.OffsetTime;

@Service
@RequiredArgsConstructor
public class TicketRowMapper implements RowMapper<Ticket> {

    private final RouteRepository routeRepository;
    private final UserRepository userRepository;

    @Override
    public Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserBuyer user = userRepository.findById(rs.getLong("user_id"));
        Route route =  routeRepository.findById(rs.getLong("route_id"));

        return Ticket.builder()
                .ticketId(rs.getLong("ticket_id"))
                .route(route)
                .departureDate(rs.getTimestamp("departure_date").toLocalDateTime())
                .seatNumber(rs.getString("seat_number"))
                .user(user)
                .buyDate(rs.getObject("buy_date", LocalDateTime.class))
                .price(rs.getLong("price"))
                .build();

    }
}
