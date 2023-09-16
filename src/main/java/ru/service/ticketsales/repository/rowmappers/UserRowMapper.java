package ru.service.ticketsales.repository.rowmappers;

import org.springframework.jdbc.core.RowMapper;
import ru.service.ticketsales.models.UserBuyer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<UserBuyer> {
    @Override
    public UserBuyer mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserBuyer.builder()
                .userId(rs.getLong("user_id"))
                .login(rs.getString("login"))
                .password(rs.getString("password"))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .secondName(rs.getString("second_name"))
                .build();
    }
}
