package com.demo.crud.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String PRODUCT_EXCHANGE = "product.exchange";
    public static final String PRODUCT_CREATED_QUEUE = "product.created.queue";
    public static final String PRODUCT_UPDATED_QUEUE = "product.updated.queue";
    public static final String PRODUCT_DELETED_QUEUE = "product.deleted.queue";
    public static final String ROUTING_CREATED = "product.created";
    public static final String ROUTING_UPDATED = "product.updated";
    public static final String ROUTING_DELETED = "product.deleted";

    @Bean
    public DirectExchange productExchange() {
        return new DirectExchange(PRODUCT_EXCHANGE);
    }

    @Bean
    public Queue productCreatedQueue() {
        return new Queue(PRODUCT_CREATED_QUEUE, true);
    }

    @Bean
    public Queue productUpdatedQueue() {
        return new Queue(PRODUCT_UPDATED_QUEUE, true);
    }

    @Bean
    public Queue productDeletedQueue() {
        return new Queue(PRODUCT_DELETED_QUEUE, true);
    }

    @Bean
    public Binding productCreatedBinding(Queue productCreatedQueue, DirectExchange productExchange) {
        return BindingBuilder.bind(productCreatedQueue).to(productExchange).with(ROUTING_CREATED);
    }

    @Bean
    public Binding productUpdatedBinding(Queue productUpdatedQueue, DirectExchange productExchange) {
        return BindingBuilder.bind(productUpdatedQueue).to(productExchange).with(ROUTING_UPDATED);
    }

    @Bean
    public Binding productDeletedBinding(Queue productDeletedQueue, DirectExchange productExchange) {
        return BindingBuilder.bind(productDeletedQueue).to(productExchange).with(ROUTING_DELETED);
    }
}
