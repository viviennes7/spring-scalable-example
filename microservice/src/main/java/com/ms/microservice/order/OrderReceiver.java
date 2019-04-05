package com.ms.microservice.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Slf4j
@Component
public class OrderReceiver {
    public static final long EVENT_ITEM_ID = 16332L;
    private CountDownLatch latch = new CountDownLatch(1);
    private final OrderRepository orderRepository;

    public OrderReceiver(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void receiveMessage(Long userId) {
        log.info("received ::: " + userId);
        this.orderRepository.save(new EventOrder(EVENT_ITEM_ID, userId));

        sleep(3000L);
        this.latch.countDown();
    }

    private void sleep(Long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new IllegalStateException("sleep-error", e);
        }
    }

    CountDownLatch getLatch() {
        return this.latch;
    }
}
