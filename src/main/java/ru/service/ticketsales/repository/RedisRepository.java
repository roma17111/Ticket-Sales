package ru.service.ticketsales.repository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.service.ticketsales.models.Ticket;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
@Slf4j
@RequiredArgsConstructor
public class RedisRepository {

    private final TicketRepository ticketRepository;
    private static final String KEY = "Ticket";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations hashOperations;

    @PostConstruct
    private void init(){
        hashOperations = redisTemplate.opsForHash();
    }
    public void add(Ticket ticket) {
        hashOperations.put(KEY, ticket.getTicketId(), ticket);
    }
    public void delete(final String id) {
        hashOperations.delete(KEY, id);
    }
    public Ticket findTicketByUserId(final String id){
        return (Ticket) hashOperations.get(KEY, id);
    }
    public Map<Long, Ticket> findAllTickets(){
        return hashOperations.entries(KEY);
    }

}
