package com.example.client_register.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE = "cliente.exchange";
    public static final String ROUTING_KEY = "cliente.creado";

    public static final String EMAIL_QUEUE = "email.queue";
    public static final String PUNTOS_QUEUE = "puntos.queue";
    public static final String ENTREGA_QUEUE = "entrega.queue";

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE, true);
    }

    @Bean
    public Queue puntosQueue() {
        return new Queue(PUNTOS_QUEUE, true);
    }

    @Bean
    public Queue entregaQueue() {
        return new Queue(ENTREGA_QUEUE, true);
    }

    @Bean
    public Binding emailBinding(Queue emailQueue, TopicExchange exchange) {
        return BindingBuilder.bind(emailQueue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public Binding puntosBinding(Queue puntosQueue, TopicExchange exchange) {
        return BindingBuilder.bind(puntosQueue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public Binding entregaBinding(Queue entregaQueue, TopicExchange exchange) {
        return BindingBuilder.bind(entregaQueue).to(exchange).with(ROUTING_KEY);
    }
}
