package ru.restaurant.voting.util;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    public static final LocalDate MIN_DATE = LocalDate.of(1, 1, 1);
    public static final LocalDate MAX_DATE = LocalDate.of(3000, 1, 1);

    public DateTimeUtil() {
    }

    private static LocalDateTime adjustDateTime(LocalDate localDate, LocalDate defaultDate, LocalTime adjustTime) {
        return LocalDateTime.of(localDate != null ? localDate : defaultDate, adjustTime);
    }

    public static LocalDateTime adjustStartTime(LocalDate localDate) {
        return adjustDateTime(localDate, MIN_DATE, LocalTime.MIN);
    }

    public static LocalDateTime adjustEndTime(LocalDate localDate) {
        return adjustDateTime(localDate, MAX_DATE, LocalTime.MAX);
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static LocalDate parseLocalDate(@Nullable String str) {
        return StringUtils.isEmpty(str) ? null : LocalDate.parse(str);
    }

    public static LocalTime parseLocalTime(@Nullable String str) {
        return StringUtils.isEmpty(str) ? null : LocalTime.parse(str);
    }
}
