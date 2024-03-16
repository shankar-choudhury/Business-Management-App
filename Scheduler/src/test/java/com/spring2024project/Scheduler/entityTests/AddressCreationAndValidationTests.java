package com.spring2024project.Scheduler.entityTests;

import com.spring2024project.Scheduler.SchedulerApplication;
import com.spring2024project.Scheduler.customValidatorTags.ZipCodeValidatorTag;
import com.spring2024project.Scheduler.entity.Address;
import com.spring2024project.Scheduler.exception.*;
import com.spring2024project.Scheduler.exception.ValidationException.Cause;

import static com.spring2024project.Scheduler.constantValues.State.getState;
import static com.spring2024project.Scheduler.exception.ValidationException.Cause.*;
import static com.spring2024project.Scheduler.entity.Address.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SchedulerApplication.class)
public class AddressCreationAndValidationTests {

    @Autowired
    private ZipCodeValidatorTag zipVal;
    private Address testAddress;
    private Address secondTestAddress;

    @Before
    public void setUp() {
        testAddress = Address.builder()
                .buildingNumber("35")
                .street("School St")
                .city("Hanover")
                .state("NH")
                .zipcode("03755")
                .build();

        secondTestAddress = Address.builder()
                .buildingNumber("35")
                .street("School St")
                .city("Detroit")
                .state("MI")
                .zipcode("48202")
                .build();
    }

    @Test
    public void testValidAddress() {
        var a1 = Address.from(testAddress);
        var a2 = Address.from(secondTestAddress);
        assertTrue(zipVal.isValidAddress(a1));
        assertTrue(zipVal.isValidAddress(a2));
    }

    @Test
    public void testInvalidMapping() {
        testAddress.setCity("lebanon");
        assertFalse(zipVal.isValidAddress(testAddress));
        testAddress.setCity("hanover");
        testAddress.setState("maine");
        assertFalse(zipVal.isValidAddress(testAddress));
        testAddress.setCity("asd");
        assertFalse(zipVal.isValidAddress(testAddress));
        testAddress.setCity("hanover");
        testAddress.setState("new hampshire");
        testAddress.setZipcode("03756");
        assertFalse(zipVal.isValidAddress(testAddress));
    }

    @Test
    public void testMultipleCityMapping() {
        var multiCityAddress = new Address();
        multiCityAddress.setBuildingNumber("02");
        multiCityAddress.setStreet("roadhouse rd");
        multiCityAddress.setCity("riverview");
        multiCityAddress.setState("michigan");
        multiCityAddress.setZipcode("48193");

        var a = Address.from(multiCityAddress);

        Set<String> acceptableCities = Set.of("RIVERVIEW", "BROWNSTWN TWP", "BROWNSTOWN", "WYANDOTTE", "BROWNSTOWN TOWNSHIP", "BROWNSTOWN TWP");

        for (String city : acceptableCities) {
            a.setCity(city);
            assertTrue(zipVal.isValidAddress(a));
        }

    }

    @Test
    public void testValidParametersAddressCreation() {
        try {
            Address.from(testAddress);
            testAddress.setState("new hampshire");
            Address.from(testAddress);
            Address.from(secondTestAddress);
        } catch (IllegalArgumentException i) {
            if (i.getCause() instanceof StringValidationException s) {
                fail("Address elements should have correct format");;
            }
            else if (i.getCause() instanceof StateValidationException st) {
                fail("Address State should exist");;
            }
            else
                fail("Another unaccounted IllegalArgument was thrown");
        } catch (Exception e) {
            fail("No other exception should have been thrown");
        }
    }

    @Test
    public void testNullAddressCreation() {
        testAddress = null;
        assertThrows(NullPointerException.class, () -> from(testAddress));
    }

    // Test for possible bad inputs of Address.buildingNumber
    @Test
    public void testNullBuildingNumberFormat() {
        testAddress.setBuildingNumber(null); // Invalid building number
        assertInvalidFormatAddressCreation(NULL_STRING);
    }
    @Test
    public void testEmptyBuildingNumberFormat() {
        testAddress.setBuildingNumber(""); // Invalid building number
        assertInvalidFormatAddressCreation(EMPTY_STRING);
    }
    @Test
    public void testBlankBuildingNumberFormat() {
        testAddress.setBuildingNumber("  "); // Invalid building number
        assertInvalidFormatAddressCreation(BLANK_STRING);
    }
    @Test
    public void testInvalidBuildingNumberFormat() {
        testAddress.setBuildingNumber("123#"); // Invalid building number
        assertInvalidFormatAddressCreation(FORMAT);
    }

    // Test for possible bad inputs of Address.street
    @Test
    public void testNullStreet() {
        testAddress.setStreet(null); // Invalid building number
        assertInvalidFormatAddressCreation(NULL_STRING);
    }
    @Test
    public void testEmptyStreet() {
        testAddress.setStreet(""); // Invalid building number
        assertInvalidFormatAddressCreation(EMPTY_STRING);
    }
    @Test
    public void testBlankStreet() {
        testAddress.setStreet("  "); // Invalid building number
        assertInvalidFormatAddressCreation(BLANK_STRING);
    }
    @Test
    public void testInvalidStreetName() {
        testAddress.setStreet("1234"); // Invalid street name
        assertInvalidFormatAddressCreation(FORMAT);
    }

    // Test for possible bad inputs of Address.city
    @Test
    public void testNullCityName() {
        testAddress.setStreet(null); // Invalid building number
        assertInvalidFormatAddressCreation(NULL_STRING);
    }
    @Test
    public void testEmptyCityName() {
        testAddress.setStreet(""); // Invalid building number
        assertInvalidFormatAddressCreation(EMPTY_STRING);
    }
    @Test
    public void testBlankCityName() {
        testAddress.setStreet("  "); // Invalid building number
        assertInvalidFormatAddressCreation(BLANK_STRING);
    }
    @Test
    public void testInvalidCityName() {
        testAddress.setCity("1234"); // Invalid city name
        assertInvalidFormatAddressCreation(FORMAT);
    }

    // Test for possible bad inputs of Address.state
    @Test
    public void testNullState() {
        testAddress.setState(null); // Invalid state abbreviation
        assertInvalidFormatAddressCreation(NULL_STRING);
    }
    @Test
    public void testEmptyState() {
        testAddress.setState(""); // Invalid state abbreviation
        assertInvalidFormatAddressCreation(EMPTY_STRING);
    }
    @Test
    public void testBlankState() {
        testAddress.setState("  "); // Invalid state abbreviation
        assertInvalidFormatAddressCreation(BLANK_STRING);
    }
    @Test
    public void testInvalidState() {
        testAddress.setState("XY!"); // Invalid state abbreviation
        assertInvalidFormatAddressCreation(FORMAT);
        testAddress.setState("newYork3"); // Invalid state full name
        assertInvalidFormatAddressCreation(FORMAT);
    }
    @Test
    public void testNonExistingState() {
        testAddress.setState("XY"); // Non-existing state abbreviation
        assertInvalidAddressCreationWithState(NONEXISTING);
        testAddress.setState("newYork"); // Invalid state full name
        assertInvalidAddressCreationWithState(NONEXISTING);
        testAddress.setState("new Yorke"); // Non-existing state full name
        assertInvalidAddressCreationWithState(NONEXISTING);
    }

    // Test for possible bad inputs for Address.zipCode
    @Test
    public void testNullZipcode() {
        testAddress.setZipcode(null); // Invalid zipcode
        assertInvalidFormatAddressCreation(NULL_STRING);
    }
    @Test
    public void testEmptyZipcode() {
        testAddress.setZipcode(""); // Invalid zipcode
        assertInvalidFormatAddressCreation(EMPTY_STRING);
    }
    @Test
    public void testBlankZipcode() {
        testAddress.setZipcode(" "); // Invalid zipcode
        assertInvalidFormatAddressCreation(BLANK_STRING);
    }
    @Test
    public void testInvalidZipcode() {
        testAddress.setZipcode("123"); // Invalid zipcode
        assertInvalidFormatAddressCreation(FORMAT);
    }

    private void assertInvalidFormatAddressCreation(Cause expectedCause) {
        try {
            Address.from(testAddress);
            fail("Exception should have been thrown for invalid address");
        } catch (IllegalArgumentException i) {
            assertTrue(i.getCause() instanceof StringValidationException);
            assertEquals(expectedCause, ((StringValidationException) i.getCause()).cause());
        } catch (Exception e) {
            fail("Incorrect exception type thrown for invalid address");
        }
    }

    private void assertInvalidAddressCreationWithState(Cause expectedCause) {
        try {
            Address.from(testAddress);
            fail("Exception should have been thrown for invalid address");
        } catch (IllegalArgumentException i) {
            assertTrue(i.getCause() instanceof StateValidationException);
            assertEquals(expectedCause, ((StateValidationException) i.getCause()).cause());
        } catch (Exception e) {
            fail("Incorrect exception type thrown for invalid address");
        }
    }

    @Test
    public void testCorrectZipCodeMapping() {
        try {
            Address.from(testAddress, zipVal);
        } catch (Exception e) {
            fail("no exception should be thrown");
        }
    }

    @Test
    public void testIncorrectZipCodeStateMapping() {
        testAddress.setState("NY");
        assertNoMappingZipToState(NONEXISTING);
    }

    @Test
    public void testIncorrectZipCodeCityMapping() {
        testAddress.setCity("lebanon");
        assertNoMappingZipToCity(NONEXISTING);
    }

    private void assertNoMappingZipToState(Cause expectedCause) {
        try {
            Address.from(testAddress, zipVal);
            fail("Exception should have been thrown for address with zip that doesn't map to state");
        } catch (IllegalArgumentException i) {
            assertTrue(i.getCause() instanceof AddressValidationException);
            assertEquals(NONEXISTING, ((AddressValidationException) i.getCause()).cause());
        }
    }

    private void assertNoMappingZipToCity(Cause expectedCause) {
        try {
            Address.from(testAddress, zipVal);
            fail("Exception should have been thrown for address with zip that doesn't map to city");
        } catch (IllegalArgumentException i) {
            assertTrue(i.getCause() instanceof AddressValidationException);
            assertEquals(NONEXISTING, ((AddressValidationException) i.getCause()).cause());
        }
    }

}
