package com.mikehenry.rabbitmqdataconsumer.configuration;

import com.mikehenry.rabbitmqdataconsumer.consumer.DataConsumer;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RabbitMQConfiguration {

    RabbitConfig rabbitConfig;
    ApplicationConfiguration appConfig;

    @Bean
    CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();

        cachingConnectionFactory.setHost(rabbitConfig.getHost());
        cachingConnectionFactory.setPort(rabbitConfig.getPort());
        cachingConnectionFactory.setUsername(rabbitConfig.getUsername());
        cachingConnectionFactory.setPassword(rabbitConfig.getPassword());
        cachingConnectionFactory.setVirtualHost(rabbitConfig.getVhost());

        return cachingConnectionFactory;
    }

    @Bean
    SimpleMessageListenerContainer simpleMessageListenerContainer(DataConsumer dataConsumer) {
        SimpleMessageListenerContainer messageListenerContainer = new SimpleMessageListenerContainer();

        messageListenerContainer.setQueueNames(appConfig.getQueueName());
        messageListenerContainer.setConcurrentConsumers(3);
        messageListenerContainer.setMaxConcurrentConsumers(5);
        messageListenerContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //Tells the broker how many messages to send to each consumer in a single request.
        messageListenerContainer.setPrefetchCount(1);
        messageListenerContainer.setConnectionFactory(connectionFactory());
        messageListenerContainer.setMessageListener(dataConsumer);

        return messageListenerContainer;
    }
}
