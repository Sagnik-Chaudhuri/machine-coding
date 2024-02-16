package com.parking.service;

import com.parking.exception.UserException;
import com.parking.models.*;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ParkingLotInMemoryService implements ParkingLotService{
    // TODO create repo and validators. Service should call these
    Map<String, ParkingLot> idToParkingLotMap = new HashMap<>();
    ParkingTicketService parkingTicketService = new ParkingTicketInMemoryService();

    private Map<Integer, List<ParkingSlot>> getAllParkingSlotsPerFloorPerCarTypePerBookedStatus(CarType carType, boolean isBooked, String parkingLotId) {
        return idToParkingLotMap.get(parkingLotId)
                .getParkingFloors()
                .stream()
                .flatMap(floor -> floor.getParkingSlots().stream())
                .filter(slot -> slot.isBooked() == isBooked && slot.getParkingSlotCarType() == carType)
                .collect(Collectors.groupingBy(ParkingSlot::getFloorNumber));
    }

    @Override
    public boolean createParkingLot(ParkingLot parkingLot) {
        if (idToParkingLotMap.containsKey(parkingLot.getParkingLotId())) {
            log.error("Parking Lot already created");
            return false;
        }
        idToParkingLotMap.put(parkingLot.getParkingLotId(), parkingLot);
        return true;
    }

    @Override
    public boolean addFloorToParkingLot(String parkingLotId, ParkingFloor parkingFloor) throws UserException {
        ParkingLot parkingLot = idToParkingLotMap.getOrDefault(parkingLotId, null);
        if (Objects.isNull(parkingLot)) {
            log.error("Parking Lot does not exist");
            throw new UserException("Parking Lot does not exist");
        }

        int floorsAdded = parkingLot.getParkingFloors().size();
        parkingFloor.setFloorNumber(floorsAdded);
        boolean isFloorAdded = parkingLot.getParkingFloors().add(parkingFloor);
        if (!isFloorAdded) {
            System.out.println("Floor cannot be added to lot id: " + parkingLotId);
        }

        return isFloorAdded;
    }

    @Override
    public boolean addSlotToParkingLotFloor(String parkingLotId, int floorNumber, ParkingSlot parkingSlot) {
        if (!idToParkingLotMap.containsKey(parkingLotId)) {
            System.out.println("Parking Lot does not exist");
            return false;
        }
        // TODO add validations for parkingSlot as per given problem statement

        ParkingLot parkingLot = idToParkingLotMap.get(parkingLotId);
        ParkingFloor parkingFloor = parkingLot.getParkingFloors().get(floorNumber);

        int slots = parkingFloor.getParkingSlots().size();
        parkingSlot.setBooked(false);
        parkingSlot.setFloorNumber(floorNumber);
        parkingSlot.setSlotNumber(slots);
        parkingSlot.setParkingLotId(parkingLotId);

        boolean isSlotAdded =  parkingFloor.getParkingSlots().add(parkingSlot);
        if (!isSlotAdded) {
            System.out.println("Slot cannot be added to lot id: " + parkingLotId + " and floor id: " + floorNumber);
        }

        // better way to handle this?
        CarType carType = parkingSlot.getParkingSlotCarType();
        switch (carType) {
            case CAR:
                parkingLot.getNextAvailableCarSlot().add(parkingSlot);
                break;
            case BIKE:
                parkingLot.getNextAvailableBikeSlot().add(parkingSlot);
                break;
            case TRUCK:
                parkingLot.getNextAvailableTruckSlot().add(parkingSlot);
                break;
        }

        return isSlotAdded;
    }

    @Override
    public ParkingTicket bookParkingSlot(String parkingLotId, Car car) {
        if (!idToParkingLotMap.containsKey(parkingLotId)) {
            System.out.println("Parking Lot does not exist");
            return null;
        }
        // TODO add validations for car as per given problem statement
        ParkingLot parkingLot = idToParkingLotMap.get(parkingLotId);
        ParkingSlot nextAvailableSlot = null;
        CarType carType = car.getCarType();
        switch (carType) {
            case CAR:
                nextAvailableSlot = parkingLot.getNextAvailableCarSlot().poll();
                break;
            case BIKE:
                nextAvailableSlot = parkingLot.getNextAvailableBikeSlot().poll();
                break;
            case TRUCK:
                nextAvailableSlot = parkingLot.getNextAvailableTruckSlot().poll();
                break;
        }
        if (Objects.isNull(nextAvailableSlot)) {
            System.out.println("no available slot found for car: " + car);
            return null;
        }

        else {
            nextAvailableSlot.setBooked(true);
            // should this be called from controller directly?
            return parkingTicketService.bookTicket(car, nextAvailableSlot);
        }

    }

    @Override
    public boolean unBookParkingSlot(ParkingTicket parkingTicket) {
        // should this be called from controller directly?
        ParkingSlot parkingSlot = parkingTicketService.unBookTicket(parkingTicket).getParkingSlot();;

        parkingSlot.setBooked(false);
        Car car = parkingTicket.getCar();
        ParkingLot parkingLot = idToParkingLotMap.getOrDefault(parkingSlot.getParkingLotId(), null);


        if (Objects.isNull(parkingLot)) {
            // TODO add any other validations if any
            System.out.println("parking lot id not found");
            return false;
        }

        switch (car.getCarType()) {
            case CAR:
                parkingLot.getNextAvailableCarSlot().add(parkingSlot);
                break;
            case TRUCK:
                parkingLot.getNextAvailableTruckSlot().add(parkingSlot);
                break;
            case BIKE:
                parkingLot.getNextAvailableBikeSlot().add(parkingSlot);
                break;
        }
        return true;
    }

    @Override
    public Map<Integer, Integer> numberOfFreeSlotsPerFloorPerCarType(CarType carType, String parkingLotId) {
        Map<Integer, List<ParkingSlot>> floorToParkingSlot = availableParkingSlotsPerFloorPerCarType(carType, parkingLotId);
        return floorToParkingSlot
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().size()));
    }

    @Override
    public Map<Integer, List<ParkingSlot>> availableParkingSlotsPerFloorPerCarType(CarType carType, String parkingLotId) {
        ParkingLot parkingLot = idToParkingLotMap.getOrDefault(parkingLotId, null);
        if (Objects.isNull(parkingLot)) {
            System.out.println("invalid parking lot id: "+ parkingLotId);
            return null;
        }

        return getAllParkingSlotsPerFloorPerCarTypePerBookedStatus(carType, false, parkingLotId);
    }

    @Override
    public Map<Integer, List<ParkingSlot>> occupiedParkingSlotsPerFloor(CarType carType, String parkingLotId) {
        ParkingLot parkingLot = idToParkingLotMap.getOrDefault(parkingLotId, null);
        if (Objects.isNull(parkingLot)) {
            System.out.println("invalid parking lot id: "+ parkingLotId);
            return null;
        }

        return getAllParkingSlotsPerFloorPerCarTypePerBookedStatus(carType, true, parkingLotId);
    }
}
