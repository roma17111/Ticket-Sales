package ru.service.ticketsales.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.service.ticketsales.models.Carrier;
import ru.service.ticketsales.repository.rowmappers.CarrierRowMapper;

@Repository
@RequiredArgsConstructor
public class CarrierRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final CarrierRowMapper carrierRowMapper;

    public Long save(Carrier carrier) {
        Carrier carrier1 = findByName(carrier.getCarrierName());
        if (carrier1 == null) {
           return createCarrier(carrier);
        } else {
          return   updateCarrier(Carrier.builder()
                    .carrierId(carrier1.getCarrierId())
                    .carrierName(carrier.getCarrierName())
                    .phoneNumber(carrier.getPhoneNumber())
                    .build());
        }
    }

    public Long createCarrier(Carrier carrier) {
        String sql = "INSERT INTO carriers(carrier_name, phone_number)" +
                "values(:carrier_name, :phone_number) returning carrier_id";
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("carrier_name", carrier.getCarrierName())
                .addValue("phone_number", carrier.getPhoneNumber());
        return jdbcTemplate.queryForObject(sql, source, Long.class);
    }

    public Long updateCarrier(Carrier carrier) {
        String sql = "update carriers set " +
                "carrier_name = :carrier_name," +
                "phone_number = :phone_number " +
                "where carrier_id = :carrier_id";
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("carrier_id", carrier.getCarrierId())
                .addValue("carrier_name", carrier.getCarrierName())
                .addValue("phone_number", carrier.getPhoneNumber());
        jdbcTemplate.update(sql, source);
        return carrier.getCarrierId();
    }

    public Carrier findById(long id) {
        try {
            String sql = "SELECT * FROM carriers WHERE carrier_id = :carrier_id";
            MapSqlParameterSource source = new MapSqlParameterSource("carrier_id", id);
            return jdbcTemplate.queryForObject(sql, source, this.carrierRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Carrier findByName(String name) {
        try {
            String sql = "SELECT * FROM carriers WHERE carrier_name = :carrier_name";
            MapSqlParameterSource source = new MapSqlParameterSource("carrier_name", name);
            return jdbcTemplate.queryForObject(sql, source, this.carrierRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


}
