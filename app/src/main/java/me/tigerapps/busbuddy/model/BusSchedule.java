package me.tigerapps.busbuddy.model;

import org.threeten.bp.OffsetTime;

import java.util.TreeMap;

/**
 * Immutable object that represents a bus schedule: a mapping from time of day to bus location.
 */

public class BusSchedule extends TreeMap<OffsetTime, BusStop> {
}
