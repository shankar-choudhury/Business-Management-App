package com.spring2024project.Scheduler.serviceTests;

import com.spring2024project.Scheduler.customValidatorTags.ZipCodeValidatorTag;
import com.spring2024project.Scheduler.entity.Address;
import com.spring2024project.Scheduler.entity.CreditCard;
import com.spring2024project.Scheduler.entity.Customer;
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

import static com.spring2024project.Scheduler.exception.ValidationException.Cause.FORMAT;
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

        validAddress = new Address();
        validAddress.setId(4);
        validAddress.setBuildingNumber("1");
        validAddress.setStreet("cedric st");
        validAddress.setCity("hanover");
        validAddress.setState("new hampshire");
        validAddress.setZipcode("03755");

        secondValidAddress = new Address();
        secondValidAddress.setBuildingNumber("17");
        secondValidAddress.setStreet("cedrices st");
        secondValidAddress.setCity("norwich");
        secondValidAddress.setState("vermont");
        secondValidAddress.setZipcode("05055");

        invalidAddress = new Address();
        invalidAddress.setBuildingNumber("4");
        invalidAddress.setStreet("cedric st");
        invalidAddress.setCity("Lebanon");
        invalidAddress.setState("new hampshire");
        invalidAddress.setZipcode("03755");

        validCC = new CreditCard();
        validCC.setId(1);
        validCC.setNumber("4400665614591913");
        validCC.setExpMonth(6);
        validCC.setExpYear(2028);
        validCC.setBillingAddress(validAddress);
        validCC.setCustomer(owner);

        invalidCC = new CreditCard();
        invalidCC.setNumber("1234567890123456");
        invalidCC.setExpMonth(6);
        invalidCC.setExpYear(2028);
        invalidCC.setBillingAddress(validAddress);
        invalidCC.setCustomer(owner);

        invalidAddressCC = new CreditCard();
        invalidAddressCC.setNumber("4400665614591913");
        invalidAddressCC.setExpMonth(6);
        invalidAddressCC.setExpYear(2028);
        invalidAddressCC.setBillingAddress(invalidAddress);
        invalidAddressCC.setCustomer(owner);

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
        var l = ccs.getAll();
        assertNotEquals(original, updated);
        assertEquals(updated, l.getFirst());
    }

}
