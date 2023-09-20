package ru.service.ticketsales.models;

import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(exclude = "carrierId")
public class Carrier implements Serializable {

    long carrierId;
    String carrierName;

    @Pattern(regexp = "^\\+?[1-9][0-9]{7,14}$")
    String phoneNumber;
}
