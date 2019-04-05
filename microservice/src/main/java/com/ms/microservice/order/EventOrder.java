package com.ms.microservice.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class EventOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long itemId;

    private Long userId;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    public EventOrder(Long itemId, Long userId) {
        this.itemId = itemId;
        this.userId = userId;
        
        final LocalDateTime now = LocalDateTime.now();
        this.createDate = now;
        this.updateDate = now;
    }

}
