package com.ms.microservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.microservice.order.OrderReceiver;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveListOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import static com.ms.microservice.event.EventService.EVENT_APPLY_KEY;

@SpringBootApplication
public class MicroserviceApplication implements CommandLineRunner {

    public static final String EVENT_QUEUE = "event-queue";
    public static final String EVENT_TOPIC = "event-topic";

    private final ReactiveValueOperations reactiveValueOperations;

    public MicroserviceApplication(@Lazy ReactiveValueOperations reactiveValueOperations) {
        this.reactiveValueOperations = reactiveValueOperations;
    }

    public static void main(String[] args) {
        SpringApplication.run(MicroserviceApplication.class, args);
    }

    @Override
    public void run(String... args) {
        this.reactiveValueOperations.set(EVENT_APPLY_KEY, "0").subscribe();
    }

    @Bean
    public ReactiveRedisTemplate reactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory,
                                                       ObjectMapper objectMapper) {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        StringRedisSerializer defaultSerializer = new StringRedisSerializer();
        RedisSerializationContext serializationContext = RedisSerializationContext
                .newSerializationContext(defaultSerializer)
                .hashValue(jackson2JsonRedisSerializer)
                .build();

        return new ReactiveRedisTemplate(connectionFactory, serializationContext);
    }

    @Bean
    public ReactiveValueOperations reactiveValueOperations(ReactiveRedisTemplate reactiveRedisTemplate) {
        return reactiveRedisTemplate.opsForValue();
    }


    @Bean
    public ReactiveListOperations reactiveListOperations(ReactiveRedisTemplate reactiveRedisTemplate) {
        return reactiveRedisTemplate.opsForList();
    }

    @Bean
    Queue queue() {
        return new Queue(EVENT_QUEUE, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(EVENT_TOPIC);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with("foo.bar.#");
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(EVENT_QUEUE);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(OrderReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
}
