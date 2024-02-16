package com.parking.service;

import com.parking.exception.UserException;
import com.parking.models.*;

import java.util.List;
import java.util.Map;

public interface ParkingLotService {
    public boolean createParkingLot(ParkingLot parkingLot);
    public boolean addFloorToParkingLot(String parkingLotId, ParkingFloor parkingFloor) throws UserException;
    public boolean addSlotToParkingLotFloor(String parkingLotId, int floorNumber, ParkingSlot parkingSlot);
    public ParkingTicket bookParkingSlot(String parkingLotId, Car car);
    public boolean unBookParkingSlot(ParkingTicket parkingTicket);
    public Map<Integer, Integer> numberOfFreeSlotsPerFloorPerCarType(CarType carType, String parkingLotId);
    public Map<Integer, List<ParkingSlot>> availableParkingSlotsPerFloorPerCarType(CarType carType, String parkingLotId);
    public Map<Integer, List<ParkingSlot>> occupiedParkingSlotsPerFloor(CarType carType, String parkingLotId);
    
}
