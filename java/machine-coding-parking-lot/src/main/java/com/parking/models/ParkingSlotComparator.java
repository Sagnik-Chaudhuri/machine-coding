package com.parking.models;

import java.util.Comparator;

public class ParkingSlotComparator implements Comparator<ParkingSlot> {

    @Override
    public int compare(ParkingSlot o1, ParkingSlot o2) {
        // First, check if either slot is booked
        if (o1.isBooked() && !o2.isBooked()) {
            return 1; // o1 is booked, so o2 is considered "less"
        } else if (!o1.isBooked() && o2.isBooked()) {
            return -1; // o2 is booked, so o1 is considered "less"
        } else if (!o1.isBooked() && !o2.isBooked()) { // Both are unbooked
            // Compare by floor number
            if (o1.getFloorNumber() < o2.getFloorNumber()) {
                return -1; // o1 is on a lower floor
            } else if (o1.getFloorNumber() > o2.getFloorNumber()) {
                return 1; // o1 is on a higher floor
            } else { // Same floor number
                // Compare by slot number
                return Integer.compare(o1.getSlotNumber(), o2.getSlotNumber());
            }
        } else { // Both are booked, considered equal
            return 0;
        }
    }
}
