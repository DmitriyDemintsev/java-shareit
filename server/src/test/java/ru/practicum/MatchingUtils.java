package ru.practicum;

import org.hamcrest.Matcher;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;

public class MatchingUtils {

    private MatchingUtils() {};

    public static Matcher<String> isLocalDateTime(
            final LocalDateTime localDateTime
    ) {
        final String rawString = localDateTime.toString();
        int position = rawString.length();
        while (position - 1 >= 0 && rawString.charAt(position - 1) == '0') {
            --position;
        }
        final String trimmed = rawString.substring(0, position);
        return is(trimmed);
    }
}
