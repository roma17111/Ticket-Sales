package ru.service.ticketsales.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserBuyer {

    long userId;
    String login;
    String password;
    String firstName;
    String lastName;
    String secondName;
}
