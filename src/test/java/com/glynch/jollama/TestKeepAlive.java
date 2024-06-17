package com.glynch.jollama;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.glynch.jollama.KeepAlive.Units;

class TestKeepAlive {

    @Test
    void keeAlive1Second() {
        KeepAlive keepAlive = KeepAlive.seconds(1);
        assertEquals(1, keepAlive.duration());
    }

    @Test
    void keeAlive1Minute() {
        KeepAlive keepAlive = KeepAlive.minutes(1);
        assertEquals(60, keepAlive.duration());
    }

    @Test
    void keeAlive1Day() {
        KeepAlive keepAlive = KeepAlive.days(1);
        assertEquals(86400, keepAlive.duration());
    }

    @Test
    void keeAlive1Hour() {
        KeepAlive keepAlive = KeepAlive.hours(1);
        assertEquals(3600, keepAlive.duration());
    }

    @Test
    void keeAliveForever() {
        KeepAlive keepAlive = KeepAlive.FOREVER;
        assertEquals(-1, keepAlive.duration());
    }

    @Test
    void keeAliveNever() {
        KeepAlive keepAlive = KeepAlive.NEVER;
        assertEquals(0, keepAlive.duration());
    }

    @Test
    void keepAliveDefault() {
        KeepAlive keepAlive = KeepAlive.DEFAULT;
        assertEquals(300, keepAlive.duration());
    }

    @Test
    void keepAliveOf1s() {
        KeepAlive keepAlive = KeepAlive.of("1s");
        assertEquals(1, keepAlive.duration());
    }

    @Test
    void keepAliveOf1m() {
        KeepAlive keepAlive = KeepAlive.of("1m");
        assertEquals(60, keepAlive.duration());
    }

    @Test
    void keepAliveOf1h() {
        KeepAlive keepAlive = KeepAlive.of("1h");
        assertEquals(3600, keepAlive.duration());
    }

    @Test
    void keepAliveOf1d() {
        KeepAlive keepAlive = KeepAlive.of("1d");
        assertEquals(86400, keepAlive.duration());
    }

    @Test
    void keepAliveOf0() {
        KeepAlive keepAlive = KeepAlive.of("0");
        assertEquals(KeepAlive.NEVER, keepAlive);
    }

    @Test
    void keepAliveOfMinus1() {
        KeepAlive keepAlive = KeepAlive.of("-1");
        assertEquals(KeepAlive.FOREVER, keepAlive);
    }

    @Test
    void keepAliveOfNull() {
        assertThrows(
                NullPointerException.class,
                () -> KeepAlive.of(null));
    }

    @Test
    void keepAliveOfNegative() {
        assertThrows(
                IllegalArgumentException.class,
                () -> KeepAlive.of("-500"));
    }

    @Test
    void keepAliveOfNegativeWithUnits() {
        assertThrows(
                IllegalArgumentException.class,
                () -> KeepAlive.of("-500s"));
    }

    @Test
    void keepAliveSymbol() {
        KeepAlive keepAlive = KeepAlive.seconds(1);
        assertEquals(Units.SECONDS, keepAlive.units());
    }

    @Test
    void keepAliveNoUnits() {
        KeepAlive keepAlive = KeepAlive.of("1");
        assertAll(
                () -> assertEquals(1, keepAlive.duration()),
                () -> assertEquals(Units.SECONDS, keepAlive.units()));
    }

    @Test
    void keepAliveInvalidUnits() {
        assertThrows(
                IllegalArgumentException.class,
                () -> KeepAlive.of("1y"));
    }

    @Test
    void keepAliveInValid() {
        assertThrows(
                IllegalArgumentException.class,
                () -> KeepAlive.of("foo"));
    }

    @Test
    void keepAliveToString() {
        KeepAlive keepAlive = KeepAlive.DEFAULT;
        assertEquals("5m", keepAlive.toString());
    }

}
