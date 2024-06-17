package com.glynch.jollama;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record KeepAlive(long duration, Units units) {

    private static final Pattern PATTERN = Pattern.compile("^((0|-1)|(([1-9]\\d*)([smhd])?))$");

    public static final KeepAlive DEFAULT = KeepAlive.minutes(5);
    public static final KeepAlive FOREVER = new KeepAlive(-1);
    public static final KeepAlive NEVER = new KeepAlive(0);

    public KeepAlive(long duration) {
        this(duration, null);
    }

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

    public enum Units {
        SECONDS("s"),
        MINUTES("m"),
        HOURS("h"),
        DAYS("d");

        private final String symbol;

        private Units(String symbol) {
            this.symbol = symbol;
        }

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

    public static KeepAlive seconds(long duration) {
        return new KeepAlive(duration, Units.SECONDS);
    }

    public static KeepAlive minutes(long duration) {
        return new KeepAlive(duration, Units.MINUTES);
    }

    public static KeepAlive hours(long duration) {
        return new KeepAlive(duration, Units.HOURS);
    }

    public static KeepAlive days(long duration) {
        return new KeepAlive(duration, Units.DAYS);
    }

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
