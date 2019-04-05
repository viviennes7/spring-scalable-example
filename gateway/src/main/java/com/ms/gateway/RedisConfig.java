package com.ms.gateway;

import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Configuration
public class RedisConfig {
    private RedisServer redisServer;

    @PostConstruct
    public void redisServer() {
        this.redisServer = new RedisServer(6379);
        this.redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        if (this.redisServer != null) {
            this.redisServer.stop();
        }
    }
}
