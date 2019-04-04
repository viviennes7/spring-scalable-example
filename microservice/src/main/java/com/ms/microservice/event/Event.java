package com.ms.microservice.event;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class Event {
    private Long id;
    private Long userId;
    private boolean isPurchase;
}
