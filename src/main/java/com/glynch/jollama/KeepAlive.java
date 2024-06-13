package com.glynch.jollama;

public record KeepAlive(long duration, Units units) {

    public static final KeepAlive DEFAULT = KeepAlive.minutes(5);
    public static final KeepAlive FOREVER = new KeepAlive(-1);
    public static final KeepAlive NEVER = new KeepAlive(0);

    public KeepAlive(long duration) {
        this(duration, null);
    }

    public static enum Units {
        MILLISECONDS("ms"),
        SECONDS("s"),
        MINUTES("m"),
        HOURS("h"),
        DAYS("d");

        private final String symbol;

        private Units(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public String toString() {
            return symbol;
        }
    }

    public static KeepAlive milliseconds(long duration) {
        return new KeepAlive(duration, Units.MILLISECONDS);
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(String.valueOf(duration));
        if (units != null) {
            sb.append(units);
        }
        return sb.toString();
    }

}
