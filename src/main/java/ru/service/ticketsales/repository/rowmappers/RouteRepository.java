package ru.service.ticketsales.repository.rowmappers;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.service.ticketsales.models.Route;
import ru.service.ticketsales.repository.CarrierRepository;

@Repository
@RequiredArgsConstructor
public class RouteRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RouteRowMapper routeRowMapper;
    private final CarrierRepository carrierRepository;

    public Long save(Route route) {
        long carrierId = carrierRepository.save(route.getCarrier());
        String sql = "INSERT INTO routes(departure_point,destination, " +
                "carrier_id, duration_in_minutes) " +
                "values(:departure_point, :destination, :carrier_id, :duration_in_minutes)";
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("departure_point", route.getDeparturePoint())
                .addValue("destination", route.getDestination())
                .addValue("carrier_id", carrierId)
                .addValue("duration_in_minutes", route.getDurationInMinutes());
        return jdbcTemplate.queryForObject(sql, source, Long.class);
    }

    public Long updateRoute(Route route) {
        long carrierId = carrierRepository.save(route.getCarrier());
        String sql = "UPDATE routes SET " +
                "departure_point = :departure_point," +
                "destination = :destination," +
                "carrier_id = :carrier_id," +
                "duration_in_minutes = :duration_in_minutes " +
                "WHERE route_id = :route_id";
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("route_id", route.getRouteId())
                .addValue("departure_point", route.getDeparturePoint())
                .addValue("destination", route.getDestination())
                .addValue("carrier_id", carrierId)
                .addValue("duration_in_minutes", route.getDurationInMinutes());
        jdbcTemplate.update(sql, source);
        return route.getRouteId();
    }

    public Route findById(long id) {
        try {
            String sql = "SELECT * FROM routes WHERE route_id = :route_id";
            MapSqlParameterSource source = new MapSqlParameterSource("route_id", id);
            return jdbcTemplate.queryForObject(sql, source, this.routeRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}
