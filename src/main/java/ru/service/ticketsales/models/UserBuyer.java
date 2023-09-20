package ru.service.ticketsales.models;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.service.ticketsales.security.Role;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserBuyer implements Serializable   {

    long userId;
    String login;
    String password;
    String firstName;
    String lastName;
    String secondName;
    Set<Role> rolesSet;
}
