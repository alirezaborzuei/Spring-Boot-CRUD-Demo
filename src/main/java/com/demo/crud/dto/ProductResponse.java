package com.demo.crud.dto;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.Data;

@Data
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Instant createdAt;
    private Instant updatedAt;

    public ProductResponse() {
    }

    public ProductResponse(Long id, String name, String description, BigDecimal price, Integer stock,
                           Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
