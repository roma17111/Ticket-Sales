package ru.service.ticketsales.repository.rowmappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import ru.service.ticketsales.models.UserBuyer;
import ru.service.ticketsales.repository.UserRepository;
import ru.service.ticketsales.security.Role;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRowMapper implements RowMapper<UserBuyer> {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public UserBuyer mapRow(ResultSet rs, int rowNum) throws SQLException {
        long userId = rs.getLong("user_id");
        List<Role> roles = getRoles(userId);

        return UserBuyer.builder()
                .userId(userId)
                .login(rs.getString("login"))
                .password(rs.getString("password"))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .secondName(rs.getString("second_name"))
                .rolesSet(new HashSet<>(roles))
                .build();
    }

    public List<Role> getRoles(long userId) {
        String sql = "SELECT role FROM roles WHERE user_id = :user_id";
        MapSqlParameterSource source = new MapSqlParameterSource("user_id", userId);
        return jdbcTemplate.queryForList(sql, source, Role.class);
    }
}
