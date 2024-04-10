package com.spring2024project.Scheduler.entityTests;

import com.spring2024project.Scheduler.entityFunctionalities.address.Address;
import com.spring2024project.Scheduler.entityFunctionalities.creditcard.CreditCard;
import com.spring2024project.Scheduler.entityFunctionalities.people.customer.Customer;
import com.spring2024project.Scheduler.exception.StringValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.spring2024project.Scheduler.exception.ValidationException.Cause.FORMAT;
import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerCreationAndValidationTests {

    private Customer c;
    private Address validAddress;
    private Address invalidAddress;
    private CreditCard cc;

    @BeforeEach
    public void setUp() {
        validAddress = Address.builder()
                .buildingNumber("1")
                .street("Overlook")
                .city("Cleveland Heights")
                .state("ohio")
                .zipcode("44106")
                .build();

        c = new Customer();
        c.setFirstName("shankar");
        c.setLastName("choudhury");
        c.setEmail("sxc1782@case.edu");
        c.setPhoneNumber("6033361234");

        invalidAddress = Address.builder()
                .buildingNumber("1")
                .street("Overlook")
                .city("Lakewood")
                .state("ohio")
                .zipcode("44106")
                .build();

        cc = CreditCard.builder()
                .number("4400665614591913")
                .expMonth(4)
                .expYear(2025)
                .billingAddress(validAddress)
                .build();
    }

    @Test
    public void testValidCustomerCreation() {
        try {
            var customer = Customer.from(c);
        } catch (Throwable t) {
            fail("No exception should have been thrown");
        }
        try {
            c.setAddressList(List.of(validAddress));
            c.setCreditCardList(List.of(cc));
            var customer = Customer.from(c);
        } catch (Throwable t) {
            fail("No exception should have been thrown");
        }

        var validPhoneNumbers = List.of("(123) 456-7890", "(123)456-7890", "123-456-7890", "123.456.7890", "123 456 7890", "9876543210");
        for (var pN : validPhoneNumbers) {
            try {
                c.setPhoneNumber(pN);
                var customer = Customer.from(c);
            } catch (Throwable t) {
                System.out.println(pN);
                fail("No exception should have been thrown");
            }
        }
    }

    @Test
    public void testInvalidFirstNameCreation() {
        c.setFirstName("shan1ar");
        Exception e = assertThrows(IllegalArgumentException.class, () -> Customer.from(c));
        assertAll(
                () -> assertNotNull(e),
                () -> assertTrue(e.getCause() instanceof StringValidationException),
                () -> assertEquals(FORMAT, ((StringValidationException) e.getCause()).cause()),
                () -> assertEquals("shan1ar", ((StringValidationException) e.getCause()).getBadString())
        );
    }

    @Test
    public void testInvalidLastNameCreation() {
        c.setLastName("choudh#ry");
        Exception e = assertThrows(IllegalArgumentException.class, () -> Customer.from(c));
        assertAll(
                () -> assertNotNull(e),
                () -> assertTrue(e.getCause() instanceof StringValidationException),
                () -> assertEquals(FORMAT, ((StringValidationException) e.getCause()).cause()),
                () -> assertEquals("choudh#ry", ((StringValidationException) e.getCause()).getBadString())
        );
    }

    @Test
    public void testInvalidEmailCreation() {
        var possibleBadInputs = List.of("sxc123@case..edu", "sxc123@@case.edu", "sxc123@123@case.edu");
        for (var badEmail : possibleBadInputs) {
            c.setEmail(badEmail);
            Exception e = assertThrows(IllegalArgumentException.class, () -> Customer.from(c));
            assertAll(
                    () -> assertNotNull(e),
                    () -> assertTrue(e.getCause() instanceof StringValidationException),
                    () -> assertEquals(FORMAT, ((StringValidationException) e.getCause()).cause()),
                    () -> assertEquals(badEmail, ((StringValidationException) e.getCause()).getBadString())
            );
        }
    }

    @Test
    public void testInvalidPhoneNumberCreation() {
        var possibleBadInputs = List.of("1234567","123-456","1234-5678","123-456-78901","123#456$7890");
        for (var badPN : possibleBadInputs) {
            c.setEmail(badPN);
            Exception e = assertThrows(IllegalArgumentException.class, () -> Customer.from(c));
            assertAll(
                    () -> assertNotNull(e),
                    () -> assertTrue(e.getCause() instanceof StringValidationException),
                    () -> assertEquals(FORMAT, ((StringValidationException) e.getCause()).cause()),
                    () -> assertEquals(badPN, ((StringValidationException) e.getCause()).getBadString())
            );
        }
    }

}
