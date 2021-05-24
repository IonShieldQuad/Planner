package com.ionshield.planner.database;

import androidx.room.TypeConverter;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class TypeConverters {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @TypeConverter
    public static OffsetDateTime toOffsetDateTime(String value) {
        if (value == null) return null;
        return formatter.parse(value, OffsetDateTime::from);
    }

    @TypeConverter
    public static String fromOffsetDateTime(OffsetDateTime value) {
        if (value == null) return null;
        return value.format(formatter);
    }

    @TypeConverter
    public static Duration toDuration(long seconds) {
        return Duration.ofSeconds(seconds);
    }

    @TypeConverter
    public static long toSeconds(Duration duration) {
        if (duration == null) return 0;
        return duration.getSeconds();
    }
}
