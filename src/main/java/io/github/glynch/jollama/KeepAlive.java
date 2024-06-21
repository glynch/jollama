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
     * Create a keep alive from the given value.
     * 
     * 
     * 
     * @param value The value of the keep alive.
     * @return A keep alive representing the given value.
     */
    public static KeepAlive of(String value) {
        Objects.requireNonNull(value, "value cannot be null");
        Matcher matcher = PATTERN.matcher(value);
        if (matcher.matches()) {
            // [1-9]\d*(s|m|h|d) i.e 500s, 1m, 1h, 1d
            if (matcher.group(5) != null) {
                return new KeepAlive(Long.parseLong(matcher.group(4)), Units.of(matcher.group(5)));
                // [1-9]\d* i.e 500
            } else if (matcher.group(4) != null) {
                return seconds(Long.parseLong(matcher.group(4)));
                // 0 or -1
            } else if (matcher.group(2) != null) {
                if (matcher.group(2).equals("0")) {
                    return NEVER;
                } else if (matcher.group(2).equals("-1")) {
                    return FOREVER;
                } else {
                    throw new IllegalArgumentException("Invalid keep alive value: " + value);
                }

            } else {
                throw new IllegalArgumentException("Invalid keep alive value: " + value);
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
        }
        return sb.toString();
    }

}
