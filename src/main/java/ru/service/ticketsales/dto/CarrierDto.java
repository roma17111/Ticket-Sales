package ru.service.ticketsales.dto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CarrierDto {

    long carrierId;
    String carrierName;

    @Pattern(regexp = "^\\+?[1-9][0-9]{7,14}$")
    String phoneNumber;
}
