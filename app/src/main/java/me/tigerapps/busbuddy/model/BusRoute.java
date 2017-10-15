package me.tigerapps.busbuddy.model;

import java.util.HashMap;

/**
 * Immutable object that represents a bus route: a list of stops, and the schedule for moving
 * between them.
 */

public class BusRoute {
    private final HashMap<String, BusStop> stops = new HashMap<>();
    private final BusSchedule[] schedules = new BusSchedule[7];

    public BusRoute() {
        for (int i = 0; i < schedules.length; ++i)
            schedules[i] = new BusSchedule();
    }
}
