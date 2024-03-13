package com.spring2024project.Scheduler.entityTests;

import com.spring2024project.Scheduler.entity.ZipCodeData;
import org.junit.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ZipCodeDataTests {

    @Test
    public void testZipCodeDataCreation() {
        // Test valid input
        String zip = "12345";
        Set<String> cities = Set.of("City1", "City2");
        String state = "State";
        ZipCodeData zipCodeData = ZipCodeData.of(zip, cities, state);
        assertEquals(zip, zipCodeData.getZip());
        assertEquals(cities, zipCodeData.getAcceptableCities());
        assertEquals(state, zipCodeData.getState());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullZip() {
        ZipCodeData.of(null, Set.of(), "State");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyZip() {
        ZipCodeData.of("", Set.of(), "State");
    }

    @Test//(expected = IllegalArgumentException.class)
    public void testNullCities() {
        var newZ = ZipCodeData.of("12345", null, "State");
        assertTrue(newZ.getAcceptableCities().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullState() {
        ZipCodeData.of("12345", Set.of(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyState() {
        ZipCodeData.of("12345", Set.of(), "");
    }

    @Test
    public void testEmptyZipCodeData() {
        ZipCodeData emptyZipCodeData = ZipCodeData.emptyZipCodeData();
        assertEquals("", emptyZipCodeData.getZip());
        assertTrue(emptyZipCodeData.getAcceptableCities().isEmpty());
        assertEquals("", emptyZipCodeData.getState());
    }
}
