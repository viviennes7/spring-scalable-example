package com.ms.microservice.event;

import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static java.util.Objects.nonNull;

@Service
public class EventService {

    private static final String EVENT_APPLY_SUCCESS_SIZE = "event-apply-success-size";
    private final ReactiveValueOperations<String, Integer> reactiveValueOperations;

    public EventService(ReactiveValueOperations<String, Integer> reactiveValueOperations) {
        this.reactiveValueOperations = reactiveValueOperations;
    }

    public Mono<String> apply() {
        return this.reactiveValueOperations.get(EVENT_APPLY_SUCCESS_SIZE)
                .map(size -> nonNull(size) ? "성공하셨니다." : "실패하셨습니다.");
    }
}
