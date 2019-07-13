package ru.restaurant.voting.util;

import java.time.LocalTime;

public class Util {
    public static final LocalTime EXPIRED_TIME = LocalTime.of(11, 0);

    public static <T> T orElse(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }
}
