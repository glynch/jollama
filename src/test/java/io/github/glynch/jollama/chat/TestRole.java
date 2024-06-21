package io.github.glynch.jollama.chat;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TestRole {

    @Test
    public void testUserRoleToString() {
        Role role = Role.USER;

        assertEquals("user", role.toString());
    }

    @Test
    public void testAssistantRoleToString() {
        Role role = Role.ASSISTANT;

        assertEquals("assistant", role.toString());
    }

    @Test
    public void testSystemRoleToString() {
        Role role = Role.SYSTEM;

        assertEquals("system", role.toString());
    }

    @Test
    public void testRoleStaticFactoryFromString() {
        Role role = Role.of("user");

        assertEquals(Role.USER, role);
    }

}
