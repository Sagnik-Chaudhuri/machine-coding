package com.parking.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParkingTicket {
    int ticketNumber;
    ParkingSlot parkingSlot;
    Car car;
}
