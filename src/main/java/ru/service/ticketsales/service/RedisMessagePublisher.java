package ru.service.ticketsales.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisMessagePublisher {


    private final RedisTemplate<String, Object> redisTemplate;

    private final ChannelTopic topic;


    public void publish(final String message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }

}
