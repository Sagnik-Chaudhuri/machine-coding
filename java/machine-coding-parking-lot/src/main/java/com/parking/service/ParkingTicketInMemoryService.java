package com.parking.service;

import com.parking.models.Car;
import com.parking.models.ParkingSlot;
import com.parking.models.ParkingTicket;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ParkingTicketInMemoryService implements ParkingTicketService {

    AtomicInteger uuid = new AtomicInteger(1);
    Map<Integer, ParkingTicket> ticketIdToParkingTicket = new HashMap<>();

    @Override
    public ParkingTicket bookTicket(Car car, ParkingSlot parkingSlot) {
        // TODO validations on car and parking slot

        ParkingTicket parkingTicket = ParkingTicket
                .builder()
                .car(car)
                .parkingSlot(parkingSlot)
                // autoincrement parking ticket number
                .ticketNumber(uuid.getAndAdd(1))
                .build();

        ticketIdToParkingTicket.put(parkingTicket.getTicketNumber(), parkingTicket);
        return parkingTicket;
    }

    @Override
    public ParkingTicket unBookTicket(ParkingTicket parkingTicket) {
        return ticketIdToParkingTicket.remove(parkingTicket.getTicketNumber());
    }
}
