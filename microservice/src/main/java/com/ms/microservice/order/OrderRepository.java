package com.ms.microservice.order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<EventOrder, Long> {
}
