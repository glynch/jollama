package io.github.glynch.jollama;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Keep alive duration.
 * 
 * @param duration The duration of the keep alive.
 * @param units    The {@link Units units} of the duration.
 * 
 * @author Graham Lynch
 * 
 * 
 */
public record KeepAlive(long duration, Units units) {

    private static final Pattern PATTERN = Pattern.compile("^((0|-1)|(([1-9]\\d*)([smhd])?))$");
    private static final Pattern NEVER_FOREVER_PATTERN = Pattern.compile("^(0|-1)$");
    private static final Pattern DURATION_PATTERN = Pattern.compile("^([1-9]\\d*)([smhd])?$");

    /**
     * Default keep alive of 5 minutes.
     */
    public static final KeepAlive DEFAULT = KeepAlive.minutes(5);
    /**
     * Keep alive forever.
     */
    public static final KeepAlive FOREVER = new KeepAlive(-1);
    /**
     * Unload model immediately.
     */
    public static final KeepAlive NEVER = new KeepAlive(0);

    public KeepAlive(long duration) {
        this(duration, null);
    }

    /**
     * Get the duration in seconds.
     * 
     * @return The duration in seconds.
     */
    public long duration() {
        if (units != null) {
            switch (units) {
                case SECONDS:
                    return duration;
                case MINUTES:
                    return duration * 60;
                case HOURS:
                    return duration * 3600;
                case DAYS:
                    return duration * 86400;
                default:
                    return duration;
            }
        } else {
            return duration;
        }
    }

    /**
     * The units of the keep alive duration.
     */
    public enum Units {
        /**
         * Seconds.
         */
        SECONDS("s"),
        /**
         * Minutes.
         */
        MINUTES("m"),
        /**
         * Hours.
         */
        HOURS("h"),
        /**
         * Days.
         */
        DAYS("d");

        private final String symbol;

        private Units(String symbol) {
            this.symbol = symbol;
        }

        /**
         * Get the units from the given symbol.
         * 
         * @param symbol The symbol.
         * @return The units or {@code null} if not found.
         */
        public static Units of(String symbol) {
            Objects.requireNonNull(symbol, "symbol cannot be null");
            return Arrays.stream(values()).filter(v -> v.getSymbol().equals(symbol)).findFirst()
                    .orElse(null);
        }

        public String getSymbol() {
            return symbol;
        }

        @Override
        public String toString() {
            return symbol;
        }
    }

    /**
     * Create a keep alive of the given duration in seconds.
     * 
     * @param duration The duration in seconds
     * @return A keep alive of the given duration in {@link Units#SECONDS}.
     */
    public static KeepAlive seconds(long duration) {
        return new KeepAlive(duration, Units.SECONDS);
    }

    /**
     * Create a keep alive of the given duration in minutes.
     * 
     * @param duration The duration in minutes
     * @return A keep alive of the given duration in {@link Units#MINUTES}.
     */
    public static KeepAlive minutes(long duration) {
        return new KeepAlive(duration, Units.MINUTES);
    }

    /**
     * Create a keep alive of the given duration in hours.
     * 
     * @param duration The duration in hours
     * @return A keep alive of the given duration in {@link Units#HOURS}.
     */
    public static KeepAlive hours(long duration) {
        return new KeepAlive(duration, Units.HOURS);
    }

    /**
     * Create a keep alive of the given duration in days.
     * 
     * @param duration The duration in days
     * @return A keep alive of the given duration in {@link Units#DAYS}.
     */
    public static KeepAlive days(long duration) {
        return new KeepAlive(duration, Units.DAYS);
    }

    /**
     * 
     * Obtains a {@code KeepAlive} from a text string such as {@code 500s}.
     * 
     * <p>
     * This will parse a texttual represntation of a keep alive value.
     * including the string produced by {@code toString()}.
     * </p>
     * 
     * Examples:
     * 
     * <pre>
     *  "-1" -> {@link KeepAlive#FOREVER forever}
     *  "0" -> {@link KeepAlive#NEVER never}
     *  "500s" -> 500 seconds
     *  "1m" -> 1 minute
     *  "1h" -> 1 hour
     *  "1d" -> 1 day
     *  "500" -> 500 seconds
     * 
     * </pre>
     *
     * 
     * @param value The value of the keep alive.
     * @return A keep alive representing the given value.
     */
    public static KeepAlive of(String value) {
        Objects.requireNonNull(value, "value cannot be null");
        Matcher neverForeverMatcher = NEVER_FOREVER_PATTERN.matcher(value);

        if (neverForeverMatcher.matches()) {
            if (value.equals("0")) {
                return KeepAlive.NEVER;
            } else if (value.equals("-1")) {
                return KeepAlive.FOREVER;
            } else {
                // Do nothing should not get here
            }

        }
        Matcher durationMatcher = DURATION_PATTERN.matcher(value);
        if (durationMatcher.matches()) {
            if (durationMatcher.group(2) != null) {
                return new KeepAlive(Long.parseLong(durationMatcher.group(1)), Units.of(durationMatcher.group(2)));
            } else {
                return seconds(Long.parseLong(durationMatcher.group(1)));
            }
        } else {
            throw new IllegalArgumentException("Invalid keep alive value: " + value);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(String.valueOf(duration));
        if (units != null) {
            sb.append(units);
        } else {
            // Do nothing
        }
        return sb.toString();
    }

}
