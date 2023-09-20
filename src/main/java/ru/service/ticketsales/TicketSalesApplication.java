package ru.service.ticketsales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.service.ticketsales.repository.RedisRepository;

@SpringBootApplication
public class TicketSalesApplication {


    public static void main(String[] args) {
        SpringApplication.run(TicketSalesApplication.class, args);
        System.out.println();

    }

}
