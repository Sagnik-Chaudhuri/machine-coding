package com.parking.service;

import com.parking.models.Car;
import com.parking.models.ParkingSlot;
import com.parking.models.ParkingTicket;

public interface ParkingTicketService {
    public ParkingTicket bookTicket(Car car, ParkingSlot parkingSlot);
    public ParkingTicket unBookTicket(ParkingTicket parkingTicket);
}
