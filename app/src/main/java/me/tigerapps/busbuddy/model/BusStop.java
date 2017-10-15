package me.tigerapps.busbuddy.model;

import android.util.JsonReader;

import java.io.IOException;
import java.text.ParseException;

/**
 * Immutable object that represents a named bus stop location.
 */

public class BusStop {
    public static BusStop parseJson(final JsonReader reader) throws IOException, ParseException {
        Double latitude = null, longitude = null;
        String name = null;
        int position = 0;
        reader.beginObject();
        while (reader.hasNext()) {
            position = position + 1;
            final String property = reader.nextName();
            if ("lat".equals(property))
                latitude = reader.nextDouble();
            else if ("lon".equals(property))
                longitude = reader.nextDouble();
            else if ("name".equals(property))
                name = reader.nextString();
            else
                throw new ParseException("Unknown property " + property, position);
        }
        if (latitude == null || longitude == null || name == null)
            throw new ParseException("Missing property (need 'lat', 'lon', and 'name')", position);
        reader.endObject();
        return new BusStop(latitude, longitude, name);
    }

    private final double latitude;
    private final double longitude;
    private final String name;

    private BusStop(final double latitude, final double longitude, final String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }
}
