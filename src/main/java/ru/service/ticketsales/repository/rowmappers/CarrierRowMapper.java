package ru.service.ticketsales.repository.rowmappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.service.ticketsales.models.Carrier;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class CarrierRowMapper implements RowMapper<Carrier> {
    @Override
    public Carrier mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Carrier.builder()
                .carrierId(rs.getLong("carrier_id"))
                .carrierName(rs.getString("carrier_name"))
                .phoneNumber(rs.getString("phone_number"))
                .build();
    }
}
