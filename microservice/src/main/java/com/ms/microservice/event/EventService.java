package com.ms.microservice.event;

import com.ms.microservice.MicroserviceApplication;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.ReactiveListOperations;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static java.util.Objects.isNull;

@Service
public class EventService {
    public static final String EVENT_APPLY_KEY = "event-apply-success-size";
    private static final String EVENT_APPLY_LIST = "event-apply-list";
    private static final int PURCHASE_LIMIT = 40;

    private final ReactiveValueOperations<String, String> reactiveValueOperations;
    private final ReactiveListOperations<String, String> reactiveListOperations;
    private final RabbitTemplate rabbitTemplate;


    public EventService(ReactiveValueOperations<String, String> reactiveValueOperations, ReactiveListOperations<String, String> reactiveListOperations, RabbitTemplate rabbitTemplate) {
        this.reactiveValueOperations = reactiveValueOperations;
        this.reactiveListOperations = reactiveListOperations;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Mono<Boolean> apply(Long userId) {
        return this.reactiveValueOperations.get(EVENT_APPLY_KEY)
                .doOnNext(size -> this.rabbitTemplate.convertAndSend(MicroserviceApplication.EVENT_TOPIC, "foo.bar.baz", userId))
                .filter(this::isPurchase)
                .flatMap(s -> this.reactiveValueOperations.increment(EVENT_APPLY_KEY))
                .flatMap(s -> this.reactiveListOperations.leftPush(EVENT_APPLY_LIST, userId.toString()))
                .map(add -> true)
                .defaultIfEmpty(false);
    }

    private boolean isPurchase(String size) {
        return isNull(size) || Integer.parseInt(size) < PURCHASE_LIMIT;
    }
}
