package com.demo.crud.event;

import com.demo.crud.config.RabbitMqConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProductEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public ProductEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishCreated(Long productId) {
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.PRODUCT_EXCHANGE,
                RabbitMqConfig.ROUTING_CREATED,
                new ProductEvent("PRODUCT_CREATED", productId)
        );
    }

    public void publishUpdated(Long productId) {
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.PRODUCT_EXCHANGE,
                RabbitMqConfig.ROUTING_UPDATED,
                new ProductEvent("PRODUCT_UPDATED", productId)
        );
    }

    public void publishDeleted(Long productId) {
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.PRODUCT_EXCHANGE,
                RabbitMqConfig.ROUTING_DELETED,
                new ProductEvent("PRODUCT_DELETED", productId)
        );
    }
}
