package me.tigerapps.busbuddy.model;

/**
 * Immutable object that represents a named bus stop location.
 */

public class BusStop {
    private final Double latitude;
    private final Double longitude;
    private final String name;

    public BusStop(String name, Double latitude, Double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }
}
