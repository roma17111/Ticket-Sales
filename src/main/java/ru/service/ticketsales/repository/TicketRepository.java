package ru.service.ticketsales.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.service.ticketsales.models.Ticket;
import ru.service.ticketsales.models.UserBuyer;
import ru.service.ticketsales.repository.rowmappers.TicketRowMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public void update(Ticket ticket, long routeId) {
        String sql = "UPDATE tickets set " +
                "ticket_id = :ticket_id," +
                "route_id = :route_id," +
                "departure_date = :departure_date," +
                "seat_number = :seat_number," +
                "price = :price " +
                "where ticket_id = :ticket_id";
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("ticket_id",ticket.getTicketId())
                .addValue("route_id", routeId)
                .addValue("departure_date", ticket.getDepartureDate())
                .addValue("seat_number", ticket.getSeatNumber())
                .addValue("price", ticket.getPrice());
        jdbcTemplate.update(sql, source);
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

    public List<Ticket> findAllforSale(long page) {
        long pageResult = page - 1;
        long offset = pageResult * 20;
        String sql = "SELECT * FROM tickets WHERE user_id IS NULL offset :ofset limit 20";
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("ofset", offset);
        return jdbcTemplate.query(sql, source, this.ticketRowMapper);
    }

    public List<Ticket> findAllforSale() {
        String sql = "SELECT * FROM tickets WHERE user_id IS NULL";
        return jdbcTemplate.query(sql, this.ticketRowMapper);
    }

    public List<Ticket> getAllTicketsByCareerName(String careerName, long page) {
        List<Ticket> tickets = findAllforSale(page);
        return tickets.stream()
                .filter(el -> el
                        .getRoute()
                        .getCarrier()
                        .getCarrierName().toLowerCase()
                        .startsWith(careerName))
                .collect(Collectors.toList());
    }

    public List<Ticket> findAllTicketsByDateDeparture(LocalDateTime localDateTime,
                                                      long page) {
        List<Ticket> tickets = findAllforSale(page);
        return tickets.stream()
                .filter(el -> el.getDepartureDate().equals(localDateTime))
                .collect(Collectors.toList());

    }

    public List<Ticket> findAllTicketsByRouteDeparture(String departurePoint,
                                                       long page) {
        List<Ticket> tickets = findAllforSale(page);
        return tickets.stream()
                .filter(el -> el
                        .getRoute()
                        .getDeparturePoint()
                        .toLowerCase()
                        .startsWith(departurePoint))
                .collect(Collectors.toList());

    }

    public List<Ticket> findAllTicketsByDestination(String destination,
                                                    long page) {
        List<Ticket> tickets = findAllforSale(page);
        return tickets.stream()
                .filter(el -> el
                        .getRoute()
                        .getDestination()
                        .toLowerCase()
                        .startsWith(destination))
                .collect(Collectors.toList());

    }

    public List<Ticket> findAllByDepartureAndDestination(String departure,
                                                         String destination,
                                                         long page) {
        List<Ticket> tickets = findAllforSale(page);
        return tickets.stream()
                .filter(el -> el
                        .getRoute()
                        .getDestination()
                        .toLowerCase()
                        .startsWith(destination)

                        &&

                        el.getRoute()
                                .getDeparturePoint()
                                .toLowerCase()
                                .startsWith(departure))
                .collect(Collectors.toList());
    }

    public List<Ticket> findAllByDepartureDateAndDeparturePoint(LocalDateTime localDateTime,
                                                                String departure,
                                                                long page) {
        List<Ticket> tickets = findAllforSale(page);
        return tickets.stream()
                .filter(el -> el

                        .getRoute()
                        .getDeparturePoint()
                        .toLowerCase()
                        .startsWith(departure)
                        &&

                        el
                                .getDepartureDate()
                                .equals(localDateTime))
                .collect(Collectors.toList());
    }

    public List<Ticket> findAllByDepartureDateAndDestinationPoint(LocalDateTime localDateTime,
                                                                  String destination,
                                                                  long page) {
        List<Ticket> tickets = findAllforSale(page);
        return tickets.stream()
                .filter(el -> el

                        .getRoute()
                        .getDestination()
                        .toLowerCase()
                        .startsWith(destination)
                        &&

                        el
                                .getDepartureDate()
                                .equals(localDateTime))
                .collect(Collectors.toList());
    }

    public List<Ticket> findAllByFullFilter(LocalDateTime dateTime,
                                            String destination,
                                            String departure,
                                            long page) {
        List<Ticket> tickets = findAllforSale(page);
        return tickets.stream()
                .filter(el -> el
                        .getRoute()
                        .getDestination()
                        .toLowerCase()
                        .startsWith(destination)

                        &&

                        el.getRoute()
                                .getDeparturePoint()
                                .toLowerCase()
                                .startsWith(departure)
                        &&

                        el
                                .getDepartureDate()
                                .equals(dateTime))
                .collect(Collectors.toList());
    }

    public void buyTicket(long userId, long ticketId) {
        String sql = "UPDATE  tickets SET " +
                "buy_date = now()," +
                "user_id = :user_id " +
                "WHERE ticket_id = :ticket_id";
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("ticket_id", ticketId);
        jdbcTemplate.update(sql, source);
    }

    public List<Ticket> getUserTickets(long userId) {
        String sql = "SELECT * FROM tickets WHERE user_id = :user_id";
        MapSqlParameterSource source = new MapSqlParameterSource("user_id", userId);
        return jdbcTemplate.query(sql, source,this.ticketRowMapper);
    }

    public long count(long userId) {
        String sql = "select count(*)from tickets where user_id = :user_id";
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("user_id",userId);
        return jdbcTemplate.queryForObject(sql,sqlParameterSource,Long.class);
    }

    public void deleteById(long ticketId) {
        String sql = "DELETE FROM tickets WHERE ticket_id = :ticket_id";
        MapSqlParameterSource source = new MapSqlParameterSource("ticket_id", ticketId);
        jdbcTemplate.update(sql, source);
    }

    public void deleteByRouteId(long routeId) {
        String sql = "DELETE FROM tickets WHERE route_id = :route_id";
        MapSqlParameterSource source = new MapSqlParameterSource("route_id", routeId);
        jdbcTemplate.update(sql, source);
    }

    public List<Ticket> findAllByRouteId(long routeId) {
        String sql = "SELECT * FROM tickets WHERE route_id = :route_id";
        MapSqlParameterSource source = new MapSqlParameterSource("route_id", routeId);
        return jdbcTemplate.query(sql, source, this.ticketRowMapper);
    }

}
