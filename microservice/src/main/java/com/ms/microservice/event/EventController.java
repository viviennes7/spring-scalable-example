package com.ms.microservice.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/events")
public class EventController {

    @Value("${server.port}")
    private Integer port;

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public Mono<Boolean> apply() {
        log.info("port ::: {}", port);
        final long userId = new Random().nextLong();
        return this.eventService.apply(userId);
    }
}
