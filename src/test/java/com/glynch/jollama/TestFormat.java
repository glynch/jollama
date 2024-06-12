package com.glynch.jollama;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TestFormat {

    @Test
    public void testFormat() {

        Format format = Format.JSON;
        assertEquals("json", format.toString());
        assertEquals("json", format.getValue());
    }

    @Test
    public void tesFormatFromValue() {

        Format format = Format.of("json");
        assertEquals("json", format.toString());
        assertEquals("json", format.getValue());
    }

}
