package com.glynch.jollama;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TestFormat {

    @Test
    void formatToString() {

        Format format = Format.JSON;
        assertEquals("json", format.toString());
        assertEquals("json", format.getValue());
    }

    @Test
    void formatFromValue() {

        Format format = Format.of("json");
        assertEquals("json", format.toString());
        assertEquals("json", format.getValue());
    }

}
