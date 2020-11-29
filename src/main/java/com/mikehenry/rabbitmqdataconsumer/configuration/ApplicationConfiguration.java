package com.mikehenry.rabbitmqdataconsumer.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@AllArgsConstructor
public class ApplicationConfiguration {

    @Value("${app.rabbitmq.queue}")
    private String queueName;

    @Value("${app.url.employee.create}")
    private String employeeUrlCreate;
}