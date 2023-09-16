package ru.service.ticketsales.repository.rowmappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.service.ticketsales.models.Carrier;
import ru.service.ticketsales.models.Route;
import ru.service.ticketsales.repository.CarrierRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class RouteRowMapper implements RowMapper<Route> {

    private final CarrierRepository carrierRepository;
    @Override
    public Route mapRow(ResultSet rs, int rowNum) throws SQLException {
        Carrier carrier = carrierRepository.findById(rs.getLong("carrier_id"));
        return Route.builder()
                .routeId(rs.getLong("route_id"))
                .carrier(carrier)
                .departurePoint(rs.getString("departure_point"))
                .destination(rs.getString("destination"))
                .durationInMinutes(rs.getLong("duration_in_minutes"))
                .build();
    }
}
