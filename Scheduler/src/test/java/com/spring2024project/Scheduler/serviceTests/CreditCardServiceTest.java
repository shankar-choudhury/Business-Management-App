package com.spring2024project.Scheduler.serviceTests;

import com.spring2024project.Scheduler.customValidatorTags.ZipCodeValidatorTag;
import com.spring2024project.Scheduler.entity.Address;
import com.spring2024project.Scheduler.entity.CreditCard;
import com.spring2024project.Scheduler.entity.Customer;
import com.spring2024project.Scheduler.exception.AddressValidationException;
import com.spring2024project.Scheduler.exception.StringValidationException;
import com.spring2024project.Scheduler.repository.AddressRepository;
import com.spring2024project.Scheduler.repository.CustomerRepository;
import com.spring2024project.Scheduler.service.CreditCardService;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.LinkedList;

import static com.spring2024project.Scheduler.exception.ValidationException.Cause.FORMAT;
import static com.spring2024project.Scheduler.exception.ValidationException.Cause.NONEXISTING;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ComponentScan(basePackages = {"com.spring2024project.Scheduler", "com.spring2024project.Scheduler.customValidatorTags"})
public class CreditCardServiceTest {
    @Autowired
    private AddressRepository ar;
    @Autowired
    private CustomerRepository cr;
    @Autowired
    private ZipCodeValidatorTag zt;
    @Autowired
    private CreditCardService ccs;

    private Address validAddress;
    private Address secondValidAddress;
    private Address invalidAddress;

    private CreditCard validCC;
    private CreditCard invalidCC;
    private CreditCard invalidAddressCC;
    private Customer owner;

    @BeforeEach
    public void setUp() {
        owner = new Customer();
        owner.setFirstName("shankar");
        owner.setLastName("choudhury");
        owner.setEmail("sxc1782@gmail.com");
        owner.setPhoneNumber("6036677968");

        validAddress = Address.builder()
                .buildingNumber("1")
                .street("cedric st")
                .city("hanover")
                .state("new hampshire")
                .zipcode("03755")
                .build();

        secondValidAddress = Address.builder()
                .buildingNumber("17")
                .street("cedrices st")
                .city("norwich")
                .state("vermont")
                .zipcode("05055")
                .build();

        invalidAddress = Address.builder()
                .buildingNumber("4")
                .street("cedric st")
                .city("Lebanon")
                .state("new hampshire")
                .zipcode("03755")
                .build();

        validCC = CreditCard.builder()
                .number("4400665614591913")
                .expMonth(6)
                .expYear(2028)
                .billingAddress(validAddress)
                .customer(owner)
                .build();

        invalidCC = CreditCard.builder()
                .number("1234567890123456")
                .expMonth(6)
                .expYear(2028)
                .billingAddress(validAddress)
                .customer(owner)
                .build();

        invalidAddressCC = CreditCard.builder()
                .number("4400665614591913")
                .expMonth(6)
                .expYear(2028)
                .billingAddress(invalidAddress)
                .customer(owner)
                .build();

        ar.deleteAll();
        cr.deleteAll();
    }

    @Test
    public void testValidCreateCC() {
        var formatted = CreditCard.from(validCC);
        var createdCC = ccs.create(validCC);
        assertEquals(formatted, createdCC);
    }

    @Test
    public void testInvalidCreateCC() {
        var exception = assertThrows(IllegalArgumentException.class, () -> CreditCard.from(invalidCC));
        assertAll(
                () -> assertNotNull(exception),
                () -> assertTrue(exception.getCause() instanceof StringValidationException),
                () -> assertEquals(FORMAT, ((StringValidationException) exception.getCause()).cause()),
                () -> assertEquals("1234567890123456", ((StringValidationException) exception.getCause()).getBadString())
        );
    }

    @Transactional
    @Test
    public void testGetCC() {
        var formatted = CreditCard.from(validCC);
        var createdCC = ccs.create(validCC);
        var fetchedCC = ccs.getById(createdCC.getId());
        assertEquals(createdCC, fetchedCC);
        assertEquals(formatted, fetchedCC);
    }

    @Transactional
    @Test
    public void testValidUpdateCC() {
        var original = ccs.create(validCC);
        validCC.setBillingAddress(secondValidAddress);
        var updated = ccs.update(original.getId(), validCC);
        var l = new LinkedList<>(ccs.getAll());
        assertNotEquals(original, updated);
        assertEquals(updated, l.getFirst());
    }

    @Transactional
    @Test
    public void testInvalidUpdateCC() {
        var original = ccs.create(validCC);
        validCC.setBillingAddress(invalidAddress);
        var exception = assertThrows(IllegalArgumentException.class, () -> ccs.update(original.getId(), validCC));
        assertAll(
                () -> assertNotNull(exception),
                () -> assertTrue(exception.getCause() instanceof AddressValidationException),
                () -> assertEquals(NONEXISTING, ((AddressValidationException) exception.getCause()).cause())
        );
    }

}
