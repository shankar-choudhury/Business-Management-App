package com.spring2024project.Scheduler.entityTests;

import com.spring2024project.Scheduler.SchedulerApplication;
import com.spring2024project.Scheduler.exception.StringValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.spring2024project.Scheduler.customValidatorTags.ZipCodeValidatorTag;
import com.spring2024project.Scheduler.entityFunctionalities.address.Address;
import com.spring2024project.Scheduler.entityFunctionalities.creditcard.CreditCard;
import com.spring2024project.Scheduler.exception.AddressValidationException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static com.spring2024project.Scheduler.exception.ValidationException.Cause.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SchedulerApplication.class)
public class CreditCardCreationAndValidationTests {
    @Autowired
    private ZipCodeValidatorTag zt;
    private Address validBillingAddress;
    private Address invalidBillingAddress;
    private CreditCard cc;
    @BeforeEach
    public void setUp() {
        validBillingAddress = Address.builder()
                .buildingNumber("01")
                .street("school st")
                .city("hanover")
                .state("nh")
                .zipcode("03755")
                .build();

        invalidBillingAddress = Address.builder()
                .buildingNumber("01")
                .street("school st")
                .city("hanover")
                .state("nh")
                .zipcode("03756")
                .build();

        cc = CreditCard.builder()
                .number("4111111111111111")
                .expMonth(12)
                .expYear(2025)
                .billingAddress(validBillingAddress)
                .build();
    }

    // Test case for CreditCard::from with a valid CreditCard instance
    @Test
    public void testFromValidCreditCard() {
        cc.setBillingAddress(Address.from(cc.getBillingAddress()));
        CreditCard result = CreditCard.from(cc);
        assertEquals(cc, result);
    }

    @Test
    public void testFromInvalidCreditCardNumber() {
        cc.setNumber("1234567890123456");

        Executable executable = () -> CreditCard.from(cc);

        var exception = assertThrows(IllegalArgumentException.class, executable);
        assertAll(
                () -> assertNotNull(exception),
                () -> assertTrue(exception.getCause() instanceof StringValidationException),
                () -> assertEquals(FORMAT, ((StringValidationException) exception.getCause()).cause()),
                () -> assertEquals("1234567890123456", ((StringValidationException) exception.getCause()).getBadString())
        );
    }

    @Test
    public void testFromInvalidExpirationYear() {

        cc.setExpYear(2020);
        cc.setBillingAddress(validBillingAddress);
        // Act
        Executable executable = () -> CreditCard.from(cc);

        // Assert
        var exception = assertThrows(IllegalArgumentException.class, executable);
        assertEquals("Bad value: 2020", exception.getMessage());
    }

    @Test
    public void testFromNullBillingAddress() {
        // Arrange
        cc.setBillingAddress(null);

        Executable executable = () -> CreditCard.from(cc);

        assertThrows(NullPointerException.class, executable);
    }

    @Test
    public void testInvalidMonthCreditCard() {
        // Arrange
        cc.setExpMonth(13);
        Executable executable = () -> CreditCard.checkedFrom(cc, zt);
        var exception = assertThrows(IllegalArgumentException.class, executable);

        assertEquals("Bad value: 13", exception.getMessage());;
    }

    @Test
    public void testInvalidCreditCardAddress() {

        cc.setBillingAddress(invalidBillingAddress);
        Executable executable = () -> CreditCard.checkedFrom(cc, zt);
        var exception = assertThrows(IllegalArgumentException.class, executable);
        assertAll(
                () -> assertNotNull(exception),
                () -> assertTrue(exception.getCause() instanceof AddressValidationException),
                () -> assertEquals(NONEXISTING, ((AddressValidationException) exception.getCause()).cause())
        );
    }

    @Test
    public void testBadCreditCardAddressFormat() {
        // Arrange
        cc.setNumber("4400665614591913");
        validBillingAddress.setBuildingNumber("3!h");
        cc.setBillingAddress(validBillingAddress);

        // Act
        Executable executable = () -> CreditCard.checkedFrom(cc, zt);

        // Assert
        var exception = assertThrows(IllegalArgumentException.class, executable);
        assertAll(
                () -> assertNotNull(exception),
                () -> assertTrue(exception.getCause() instanceof StringValidationException),
                () -> assertEquals(FORMAT, ((StringValidationException) exception.getCause()).cause())
        );
    }
}
