package com.parking.controller;

import com.parking.exception.UserException;
import com.parking.models.*;
import com.parking.service.ParkingLotService;
import com.parking.service.ParkingTicketService;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public class ParkingController {
    ParkingLotService parkingLotService;
    ParkingTicketService parkingTicketService;

    public ParkingController(ParkingLotService parkingLotService, ParkingTicketService parkingTicketService) {
        this.parkingLotService = parkingLotService;
        this.parkingTicketService = parkingTicketService;
    }

    public boolean createParkingLot(ParkingLot parkingLot) {
        // TODO add exceptions, like those handled in addFloorToParkingLot
        try {
            return parkingLotService.createParkingLot(parkingLot);
        }
        catch (Exception e) {
            System.out.println("Exception in createParkingLot: " + e.getMessage() );
            return false;
        }

    }

    public boolean addFloorToParkingLot(String parkingLotId, ParkingFloor parkingFloor) {
        try {
            return parkingLotService.addFloorToParkingLot(parkingLotId, parkingFloor);
        } catch (UserException e) {
            System.out.println("UserException in addFloorToParkingLot: " + e.getMessage());
            return false;
        }
        catch (Exception e) {
            System.out.println("Exception in addFloorToParkingLot: " + e.getMessage() );
            return false;
        }
    }

    public boolean addSlotToParkingLotFloor(String parkingLotId, int floorNumber, ParkingSlot parkingSlot) {
        try {
            return parkingLotService.addSlotToParkingLotFloor(parkingLotId, floorNumber, parkingSlot);
        } catch (Exception e) {
            System.out.println("Exception in addSlotToParkingLotFloor: " + e.getMessage() );
            return false;
        }
    }

    public ParkingTicket bookParkingSlot(String parkingLotId, Car car) {
        try {
            return parkingLotService.bookParkingSlot(parkingLotId, car);
        } catch (Exception e) {
            System.out.println("Exception in bookParkingSlot: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean unBookParkingSlot(ParkingTicket parkingTicket) {
        try {
            return parkingLotService.unBookParkingSlot(parkingTicket);
        } catch (Exception e) {
            System.out.println("Exception in unBookParkingSlot: " + e.getMessage() );
            return false;
        }
    }

    public Map<Integer, Integer> numberOfFreeSlotsPerFloorPerCarType(CarType carType, String parkingLotId) {
        try {
            return parkingLotService.numberOfFreeSlotsPerFloorPerCarType(carType, parkingLotId);
        } catch (Exception e) {
            System.out.println("Exception in numberOfFreeSlotsPerFloorPerCarType: " + e.getMessage() );
            return Collections.emptyMap();
        }
    }

    public Map<Integer, List<ParkingSlot>> availableParkingSlotsPerFloorPerCarType(CarType carType, String parkingLotId) {
        try {
            return parkingLotService.availableParkingSlotsPerFloorPerCarType(carType, parkingLotId);
        } catch (Exception e) {
            System.out.println("Exception in availableParkingSlotsPerFloor: " + e.getMessage() );
            return Collections.emptyMap();
        }
    }

    public Map<Integer, List<ParkingSlot>> occupiedParkingSlotsPerFloor(CarType carType, String parkingLotId) {
        try {
            return parkingLotService.occupiedParkingSlotsPerFloor(carType, parkingLotId);
        } catch (Exception e) {
            System.out.println("Exception in occupiedParkingSlotsPerFloor: " + e.getMessage() );
            return Collections.emptyMap();
        }
    }
}
