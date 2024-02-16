package com.parking.models;

import lombok.Data;

@Data
public class Car {
    CarType carType;
    String registrationNumber;
    Color color;
}
