package com.ms.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;

@SpringBootApplication
public class MicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceApplication.class, args);
	}

	@Bean
	public ReactiveValueOperations reactiveValueOperations(ReactiveRedisTemplate reactiveRedisTemplate) {
		return reactiveRedisTemplate.opsForValue();
	}

}
