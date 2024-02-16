package com.parking.models;

import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class ParkingLot {
    String parkingLotId;
    List<ParkingFloor> parkingFloors = new ArrayList<>();
    PriorityQueue<ParkingSlot> nextAvailableCarSlot = new PriorityQueue<>(new ParkingSlotComparator());
    PriorityQueue<ParkingSlot> nextAvailableBikeSlot = new PriorityQueue<>(new ParkingSlotComparator());
    PriorityQueue<ParkingSlot> nextAvailableTruckSlot = new PriorityQueue<>(new ParkingSlotComparator());

    public Map<Integer, List<ParkingSlot>> getAllParkingSlotsPerFloorPerCarTypePerBookedStatus(CarType carType, boolean isBooked) {
        return parkingFloors
                .stream()
                .flatMap(floor -> floor.getParkingSlots().stream())
                .filter(slot -> slot.isBooked() == isBooked && slot.getParkingSlotCarType() == carType)
                .collect(Collectors.groupingBy(ParkingSlot::getFloorNumber));
    }
}
