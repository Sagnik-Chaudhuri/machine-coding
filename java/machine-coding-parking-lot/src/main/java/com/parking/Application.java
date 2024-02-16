package com.parking;

import com.parking.controller.ParkingController;
import com.parking.models.*;
import com.parking.service.ParkingLotInMemoryService;
import com.parking.service.ParkingLotService;
import com.parking.service.ParkingTicketInMemoryService;
import com.parking.service.ParkingTicketService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
@Slf4j
public class Application {
    public static void main(String[] args) {
        ParkingLotService parkingLotService = new ParkingLotInMemoryService();
        ParkingTicketService parkingTicketService = new ParkingTicketInMemoryService();

        ParkingController parkingController = new ParkingController(parkingLotService, parkingTicketService);

        String pl1 = "PL_1";

        // add new parking lot
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setParkingLotId(pl1);
        parkingController.createParkingLot(parkingLot);


        // add new floor
        ParkingFloor parkingFloor1 = new ParkingFloor();
        parkingController.addFloorToParkingLot(pl1, parkingFloor1);

        // add new car slot
        ParkingSlot parkingSlot1 = new ParkingSlot();
        parkingSlot1.setParkingSlotCarType(CarType.CAR);
        parkingController.addSlotToParkingLotFloor(pl1, parkingFloor1.getFloorNumber(), parkingSlot1);


        Map<Integer, Integer> m1 = parkingController.numberOfFreeSlotsPerFloorPerCarType(CarType.CAR, pl1);
        System.out.println("m1: " + m1);

        // add new bike slot
        ParkingSlot parkingSlot2 = new ParkingSlot();
        parkingSlot2.setParkingSlotCarType(CarType.BIKE);
        parkingController.addSlotToParkingLotFloor(pl1, parkingFloor1.getFloorNumber(), parkingSlot2);

        m1 = parkingController.numberOfFreeSlotsPerFloorPerCarType(CarType.BIKE, pl1);
        System.out.println("m1: " + m1);

        m1 = parkingController.numberOfFreeSlotsPerFloorPerCarType(CarType.TRUCK, pl1);
        System.out.println("m1: " + m1);



        // add new floor and new car slot
        ParkingFloor parkingFloor2 = new ParkingFloor();
        parkingController.addFloorToParkingLot(pl1, parkingFloor2);
        ParkingSlot parkingSlot3 = new ParkingSlot();
        parkingSlot3.setParkingSlotCarType(CarType.CAR);
        parkingController.addSlotToParkingLotFloor(pl1, parkingFloor2.getFloorNumber(), parkingSlot3);


        m1 = parkingController.numberOfFreeSlotsPerFloorPerCarType(CarType.CAR, pl1);
        System.out.println("m1: " + m1);


        Car car1 = new Car();
        car1.setCarType(CarType.CAR);
        
        Map<Integer, List<ParkingSlot>> floorToParkingSlots = parkingController.availableParkingSlotsPerFloorPerCarType(CarType.BIKE, pl1);
        System.out.println(floorToParkingSlots.toString());

        floorToParkingSlots = parkingController.availableParkingSlotsPerFloorPerCarType(car1.getCarType(), pl1);
        System.out.println(floorToParkingSlots.toString());

        ParkingTicket ticket1 = parkingController.bookParkingSlot(pl1, car1);
        System.out.println("Ticket details: " + ticket1.toString());

        floorToParkingSlots = parkingController.availableParkingSlotsPerFloorPerCarType(car1.getCarType(), pl1);
        System.out.println(floorToParkingSlots.toString());

        // unbook ticket 1
        parkingController.unBookParkingSlot(ticket1);
        floorToParkingSlots = parkingController.availableParkingSlotsPerFloorPerCarType(car1.getCarType(), pl1);
        System.out.println(floorToParkingSlots.toString());

        ticket1 = parkingController.bookParkingSlot(pl1, car1);
        System.out.println("Ticket details: " + ticket1.toString());

        // no parking slots available
        ParkingTicket ticket2 = parkingController.bookParkingSlot(pl1, car1);
        System.out.println("Ticket details: " + ticket2.toString());
        floorToParkingSlots = parkingController.availableParkingSlotsPerFloorPerCarType(car1.getCarType(), pl1);
        System.out.println(floorToParkingSlots.toString());

        // only unbook ticket 1
        parkingController.unBookParkingSlot(ticket1);

        floorToParkingSlots = parkingController.availableParkingSlotsPerFloorPerCarType(car1.getCarType(), pl1);
        System.out.println(floorToParkingSlots.toString());


        // rebook ticket1
        ticket1 = parkingController.bookParkingSlot(pl1, car1);
        System.out.println("Ticket details: " + ticket1.toString());
        floorToParkingSlots = parkingController.availableParkingSlotsPerFloorPerCarType(car1.getCarType(), pl1);
        System.out.println(floorToParkingSlots.toString());


    }
}