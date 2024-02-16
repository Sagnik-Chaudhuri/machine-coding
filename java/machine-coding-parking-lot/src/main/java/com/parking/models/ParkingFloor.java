package com.parking.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ParkingFloor {
    int floorNumber;
    List<ParkingSlot> parkingSlots = new ArrayList<>();
}
