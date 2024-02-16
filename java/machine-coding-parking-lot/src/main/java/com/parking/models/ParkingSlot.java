package com.parking.models;

import lombok.Data;

@Data
public class ParkingSlot {
    int slotNumber;
    CarType parkingSlotCarType;
    boolean isBooked;
    int floorNumber;
    String parkingLotId;
}
