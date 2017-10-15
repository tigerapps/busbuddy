package me.tigerapps.busbuddy.model;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.util.JsonReader;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeParseException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Immutable object that represents a bus route: a list of stops, and the schedule for moving
 * between them.
 */

public class BusRoute implements Comparable<BusRoute> {
    public static BusRoute parseJson(final JsonReader reader) throws IOException, ParseException {
        String name = null;
        int position = 0;
        ObservableList<Segment> schedule = null;
        List<BusStop> stops = null;
        reader.beginObject();
        while (reader.hasNext()) {
            position = position + 1;
            final String property = reader.nextName();
            if ("name".equals(property))
                name = reader.nextString();
            else if ("schedule".equals(property))
                schedule = parseSchedule(reader);
            else if ("stops".equals(property))
                stops = parseStops(reader);
            else
                throw new ParseException("Unknown property " + property, position);
        }
        if (name == null || schedule == null || stops == null)
            throw new ParseException("Missing property (need 'name', 'schedule', and 'stops')",
                    position);
        reader.endObject();
        return new BusRoute(name, schedule, stops);
    }

    private static ObservableList<Segment> parseSchedule(final JsonReader reader)
            throws IOException, ParseException {
        final ObservableList<Segment> schedule = new ObservableArrayList<>();
        reader.beginArray();
        while (reader.hasNext())
            schedule.add(parseScheduleEntry(reader));
        reader.endArray();
        return schedule;
    }

    private static List<DayOfWeek> parseScheduleDays(final JsonReader reader)
            throws IOException, ParseException {
        final List<DayOfWeek> days = new ArrayList<>(DayOfWeek.values().length);
        int position = 0;
        reader.beginArray();
        while (reader.hasNext()) {
            position = position + 1;
            final String day = reader.nextString();
            if ("mon".equals(day))
                days.add(DayOfWeek.MONDAY);
            else if ("tue".equals(day))
                days.add(DayOfWeek.TUESDAY);
            else if ("wed".equals(day))
                days.add(DayOfWeek.WEDNESDAY);
            else if ("thu".equals(day))
                days.add(DayOfWeek.THURSDAY);
            else if ("fri".equals(day))
                days.add(DayOfWeek.FRIDAY);
            else if ("sat".equals(day))
                days.add(DayOfWeek.SATURDAY);
            else if ("sun".equals(day))
                days.add(DayOfWeek.SUNDAY);
            else
                throw new ParseException("Invalid day of week " + day, position);
        }
        reader.endArray();
        return days;
    }

    private static Segment parseScheduleEntry(final JsonReader reader)
            throws IOException, ParseException {
        List<DayOfWeek> days = null;
        Integer stop = null;
        LocalTime localTime = null;
        int position = 0;
        reader.beginObject();
        while (reader.hasNext()) {
            position = position + 1;
            final String property = reader.nextName();
            if ("days".equals(property))
                days = parseScheduleDays(reader);
            else if ("stop".equals(property))
                stop = reader.nextInt();
            else if ("time".equals(property)) {
                final String time = reader.nextString();
                try {
                    localTime = LocalTime.parse(time);
                } catch (final DateTimeParseException e) {
                    throw new ParseException("Invalid time" + time, position);
                }
            } else
                throw new ParseException("Unknown property " + property, position);
        }
        if (days == null || stop == null || localTime == null)
            throw new ParseException("Missing property (need 'days', 'stop', and 'time')",
                    position);
        reader.endObject();
        return new Segment(days, stop, localTime);
    }

    private static List<BusStop> parseStops(final JsonReader reader)
            throws IOException, ParseException {
        final List<BusStop> stops = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext())
            stops.add(BusStop.parseJson(reader));
        reader.endArray();
        return stops;
    }

    private final String name;
    private final ObservableList<Segment> schedule;

    private BusRoute(final String name, final ObservableList<Segment> schedule,
                     final List<BusStop> stops) {
        this.name = name;
        this.schedule = schedule;
        for (final Segment segment : schedule)
            segment.setStop(stops.get(segment.stopId));
    }

    @Override
    public int compareTo(@NonNull final BusRoute route) {
        return name.compareTo(route.name);
    }

    public String getName() {
        return name;
    }

    public ObservableList<Segment> getSchedule() {
        return schedule;
    }

    public static class Segment {
        private final List<DayOfWeek> days;
        private BusStop stop;
        private final int stopId;
        private final LocalTime time;

        private Segment(final List<DayOfWeek> days, final int stopId, final LocalTime time) {
            this.days = days;
            this.stopId = stopId;
            this.time = time;
        }

        public List<DayOfWeek> getDays() {
            return days;
        }

        public BusStop getStop() {
            return stop;
        }

        private void setStop(final BusStop stop) {
            this.stop = stop;
        }

        public LocalTime getTime() {
            return time;
        }
    }
}
