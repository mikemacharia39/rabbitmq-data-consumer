package com.mikehenry.rabbitmqdataconsumer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataRequest {

    private String employeeName;
    private double salary;
    private int age;
}
