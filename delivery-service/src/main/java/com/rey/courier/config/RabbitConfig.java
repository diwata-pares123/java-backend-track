package com.rey.courier.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_NAME = "package-exchange";
    // --- CHANGED: Renamed so RabbitMQ creates a fresh queue with our new rules ---
    public static final String NOTIFICATION_QUEUE = "notification-queue-secure"; 
    public static final String ANALYTICS_QUEUE = "analytics-queue";

    // --- NEW: DLX and DLQ Constants ---
    public static final String DLX_NAME = "package-dlx";
    public static final String DLQ_NAME = "dead-letter-queue";

    @Bean
    public FanoutExchange packageExchange() {
        return new FanoutExchange(EXCHANGE_NAME);
    }

    // --- NEW: Create the Dead Letter Exchange and Queue ---
    @Bean
    public DirectExchange deadLetterExchange() { 
        return new DirectExchange(DLX_NAME); 
    }

    @Bean
    public Queue deadLetterQueue() { 
        return new Queue(DLQ_NAME); 
    }

    @Bean
    public Binding deadLetterBinding() {
        // Any message sent to the DLX gets routed to the DLQ
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with("dead-letter");
    }

    // --- UPDATED: Add DLQ rules to the Notification Queue ---
    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(NOTIFICATION_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX_NAME)
                .withArgument("x-dead-letter-routing-key", "dead-letter")
                .build();
    }

    @Bean
    public Queue analyticsQueue() {
        return new Queue(ANALYTICS_QUEUE);
    }

    @Bean
    public Binding notificationBinding() {
        return BindingBuilder.bind(notificationQueue()).to(packageExchange());
    }

    @Bean
    public Binding analyticsBinding() {
        return BindingBuilder.bind(analyticsQueue()).to(packageExchange());
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.INFERRED);
        typeMapper.addTrustedPackages("*");
        converter.setJavaTypeMapper(typeMapper);
        
        return converter;
    }
}