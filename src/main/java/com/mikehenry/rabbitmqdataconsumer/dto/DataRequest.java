package com.mikehenry.rabbitmqdataconsumer.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class DataRequest {

    private String employeeName;
    private double salary;
    private int age;
}
