package ru.service.ticketsales.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.service.ticketsales.models.Ticket;
import ru.service.ticketsales.models.UserBuyer;
import ru.service.ticketsales.repository.rowmappers.TicketRowMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class TicketRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;
    private final RouteRepository routeRepository;
    private final TicketRowMapper ticketRowMapper;

    public Long createTicket(Ticket ticket) {
        long routeId = routeRepository.save(ticket.getRoute());

        String sql = "INSERT INTO tickets(route_id,departure_date," +
                "seat_number, price) " +
                "values(:route_id, :departure_date, :seat_number, :price)" +
                "returning ticket_id";
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("route_id", routeId)
                .addValue("departure_date", ticket.getDepartureDate())
                .addValue("seat_number", ticket.getSeatNumber())
                .addValue("price", ticket.getPrice());
        return jdbcTemplate.queryForObject(sql, source, Long.class);
    }

    public void addTicketToUser(UserBuyer buyer, long ticketId) {
        String sql = "UPDATE tickets SET " +
                "user_id = :user_id " +
                "where  ticket_id = :ticket_id";
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("user_id", buyer.getUserId())
                .addValue("ticket_id", ticketId);
        jdbcTemplate.update(sql, source);
    }

    public Ticket findById(long id) {
        try {
            String sql = "SELECT * FROM tickets WHERE ticket_id = :ticket_id";
            MapSqlParameterSource source = new MapSqlParameterSource("ticket_id", id);
            return jdbcTemplate.queryForObject(sql, source, this.ticketRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Ticket> findAll() {
        /*Ticket ticket = new Ticket();
        Map<String, Object> map = new HashMap<>();
        map.put("ticket_id", ticket.getTicketId());
        map.put("route_id", ticket.getRoute().getRouteId());
        map.put("departure_date", ticket.getDepartureDate());
        map.put("seat_number", ticket.getSeatNumber());
        map.put("user_id", ticket.getUser().getUserId());
        map.put("buy_date", ticket.getBuyDate());
        map.put("price", ticket.getPrice());*/
        String sql = "SELECT * FROM tickets";
        return jdbcTemplate.query(sql,this.ticketRowMapper);
    }


}
