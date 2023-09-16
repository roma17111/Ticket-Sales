package ru.service.ticketsales.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.service.ticketsales.models.UserBuyer;
import ru.service.ticketsales.repository.rowmappers.UserRowMapper;

import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;


    public void save(UserBuyer userBuyer) {
        UserBuyer buyer = findByLogin(userBuyer.getLogin());
        if (buyer == null) {
            createUser(userBuyer);
        } else {
            updateUser(UserBuyer.builder()
                    .userId(buyer.getUserId())
                    .password(userBuyer.getPassword())
                    .login(userBuyer.getLogin())
                    .firstName(userBuyer.getFirstName())
                    .lastName(userBuyer.getLastName())
                    .secondName(userBuyer.getSecondName())
                    .build());
        }
    }

    public Long createUser(UserBuyer userBuyer) {
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

    public void updateUser(UserBuyer userBuyer) {
        String sql = "UPDATE users SET " +
                "login = :login," +
                "password = :password," +
                "first_name = :first_name," +
                "last_name = :last_name," +
                "second_name = :second_name " +
                "WHERE user_id = :user_id" ;
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", userBuyer.getUserId());
        map.put("login", userBuyer.getLogin());
        map.put("password", userBuyer.getPassword());
        map.put("first_name", userBuyer.getFirstName());
        map.put("last_name", userBuyer.getLastName());
        map.put("second_name", userBuyer.getSecondName());
        jdbcTemplate.update(sql, map);
    }



    public UserBuyer findByLogin(String login) {
        try {
            String sql = "SELECT * FROM users WHERE login = :login";
            SqlParameterSource parameterSource = new MapSqlParameterSource("login", login);
            return jdbcTemplate.queryForObject(sql, parameterSource, this.userRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public UserBuyer findById(Long id) {
        try {
            String sql = "SELECT * FROM users WHERE user_id = :user_id";
            MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue("user_id", id);
            return jdbcTemplate.queryForObject(sql, parameterSource, this.userRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void deleteById(long id) {
        String sql = "DELETE FROM users WHERE user_id = :user_id";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource("user_id",id);
        jdbcTemplate.update(sql, mapSqlParameterSource);
    }
}
