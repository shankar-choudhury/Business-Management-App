package com.spring2024project.Scheduler;

import com.spring2024project.Scheduler.exception.StringValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.spring2024project.Scheduler.customValidatorTags.ZipCodeValidatorTag;
import com.spring2024project.Scheduler.entity.Address;
import com.spring2024project.Scheduler.entity.CreditCard;
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
    @BeforeEach
    public void setUp() {
        validBillingAddress = new Address();
        validBillingAddress.setBuildingNumber("01");
        validBillingAddress.setStreet("school st");
        validBillingAddress.setCity("hanover");
        validBillingAddress.setState("nh");
        validBillingAddress.setZipcode("03755");

        invalidBillingAddress = new Address();
        invalidBillingAddress.setBuildingNumber("01");
        invalidBillingAddress.setStreet("school st");
        invalidBillingAddress.setCity("hanover");
        invalidBillingAddress.setState("nh");
        invalidBillingAddress.setZipcode("03756");
    }

    // Helper method to create an Address instance for testing
    private Address createTestAddress() {
        return Address.emptyAddress();
    }

    // Test case for CreditCard::from with a valid CreditCard instance
    @Test
    public void testFromValidCreditCard() {
        // Arrange
        CreditCard cc = new CreditCard();
        cc.setNumber("4111111111111111");
        cc.setExpMonth(12);
        cc.setExpYear(2025);
        cc.setBillingAddress(validBillingAddress);

        var formattedAddress = Address.from(cc.getBillingAddress());

        // Act
        CreditCard result = CreditCard.from(cc);

        // Assert
        assertEquals(cc.getNumber(), result.getNumber());
        assertEquals(cc.getExpMonth(), result.getExpMonth());
        assertEquals(cc.getExpYear(), result.getExpYear());
        assertEquals(result.getBillingAddress(), formattedAddress);
    }

    @Test
    public void testFromInvalidCreditCardNumber() {
        CreditCard cc = new CreditCard();
        cc.setNumber("1234567890123456");
        cc.setExpMonth(12);
        cc.setExpYear(2025);
        cc.setBillingAddress(validBillingAddress);

        // Act
        Executable executable = () -> CreditCard.from(cc);

        // Assert
        var exception = assertThrows(IllegalArgumentException.class, executable);
        assertAll(
                () -> assertNotNull(exception),
                () -> assertTrue(exception.getCause() instanceof StringValidationException),
                () -> assertEquals(FORMAT, ((StringValidationException) exception.getCause()).cause())
        );
    }

    @Test
    public void testFromInvalidExpirationYear() {
        // Arrange
        CreditCard cc = new CreditCard();
        cc.setNumber("4111111111111111");
        cc.setExpMonth(8);
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
        CreditCard cc = new CreditCard();
        cc.setNumber("4111111111111111");
        cc.setExpMonth(10);
        cc.setExpYear(2025);
        cc.setBillingAddress(null);

        // Act
        Executable executable = () -> CreditCard.from(cc);

        // Assert
        assertThrows(NullPointerException.class, executable);
    }

    @Test
    public void testInvalidMonthCreditCard() {
        // Arrange
        CreditCard cc = new CreditCard();
        cc.setNumber("4111111111111111");
        cc.setExpMonth(13);
        cc.setExpYear(2025);
        cc.setBillingAddress(validBillingAddress);

        // Act
        Executable executable = () -> CreditCard.checkedFrom(cc, zt);
        var exception = assertThrows(IllegalArgumentException.class, executable);

        // Assert
        assertEquals("Bad value: 13", exception.getMessage());;
    }

    @Test
    public void testInvalidCreditCardAddress() {
        // Arrange
        CreditCard cc = new CreditCard();
        cc.setNumber("4400665614591913");
        cc.setExpMonth(11);
        cc.setExpYear(2025);
        cc.setBillingAddress(invalidBillingAddress);

        // Act
        Executable executable = () -> CreditCard.checkedFrom(cc, zt);

        // Assert
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
        CreditCard cc = new CreditCard();
        cc.setNumber("4400665614591913");
        cc.setExpMonth(11);
        cc.setExpYear(2025);
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
