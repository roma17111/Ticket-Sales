package ru.service.ticketsales;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.service.ticketsales.models.UserBuyer;

import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public Long save(UserBuyer userBuyer) {
        String sql = "INSERT INTO users(login, password, first_name, last_name, second_name)" +
                "VALUES(:login, :password, :first_name, :last_name, :second_name)" +
                "RETURNING user_id";
        Map<String, Object> map = new HashMap<>();
        map.put("login", userBuyer.getLogin());
        map.put("password", userBuyer.getPassword());
        map.put("first_name", userBuyer.getFirstName());
        map.put("last_name", userBuyer.getLastName());
        map.put("second_name", userBuyer.getSecondName());
        return jdbcTemplate.queryForObject(sql,map, Long.class);
    }
}
