package com.demo.crud.event;

import java.io.Serializable;
import java.time.Instant;
import lombok.Data;

@Data
public class ProductEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private String type;
    private Long productId;
    private Instant occurredAt;

    public ProductEvent() {
    }

    public ProductEvent(String type, Long productId) {
        this.type = type;
        this.productId = productId;
        this.occurredAt = Instant.now();
    }

    public ProductEvent(String type, Long productId, Instant occurredAt) {
        this.type = type;
        this.productId = productId;
        this.occurredAt = occurredAt;
    }
}
