package com.ms.gateway;

import io.arivera.oss.embedded.rabbitmq.EmbeddedRabbitMq;
import io.arivera.oss.embedded.rabbitmq.EmbeddedRabbitMqConfig;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static java.util.Objects.nonNull;

@Configuration
public class RabbitMQConfig {

    private EmbeddedRabbitMq rabbitMq;

    @PostConstruct
    private void setup() {
        EmbeddedRabbitMqConfig config = new EmbeddedRabbitMqConfig.Builder()
                .rabbitMqServerInitializationTimeoutInMillis(1_000_000L)
                .build();
        this.rabbitMq = new EmbeddedRabbitMq(config);
        this.rabbitMq.start();
    }

    @PreDestroy
    private void shutup() {
        if (nonNull(this.rabbitMq)) {
            this.rabbitMq.stop();
        }
    }
}
