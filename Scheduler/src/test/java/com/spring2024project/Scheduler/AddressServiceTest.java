package com.spring2024project.Scheduler;

import com.spring2024project.Scheduler.entity.Address;
import com.spring2024project.Scheduler.exception.AddressValidationException;
import com.spring2024project.Scheduler.repository.AddressRepository;
import com.spring2024project.Scheduler.customValidatorTags.ZipCodeValidatorTag;
import com.spring2024project.Scheduler.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.spring2024project.Scheduler.exception.ValidationException.Cause.NONEXISTING;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ComponentScan(basePackages = {"com.spring2024project.Scheduler", "com.spring2024project.Scheduler.customValidatorTags"})
public class AddressServiceTest {

    @Autowired
    private AddressRepository ar;
    @Autowired
    private ZipCodeValidatorTag zt;
    @Autowired
    private AddressService as;

    private Address validAddress;
    private Address invalidAddress;

    @BeforeEach
    public void setUp() {
        validAddress = new Address();
        validAddress.setId(1);
        validAddress.setBuildingNumber("1");
        validAddress.setStreet("cedric st");
        validAddress.setCity("hanover");
        validAddress.setState("new hampshire");
        validAddress.setZipcode("03755");

        invalidAddress = new Address();
        invalidAddress.setBuildingNumber("4");
        invalidAddress.setStreet("cedric st");
        invalidAddress.setCity("Lebanon");
        invalidAddress.setState("new hampshire");
        invalidAddress.setZipcode("03755");
    }

    @Test
    public void testValidCreateAddress() {
        var formatted = Address.from(validAddress);
        Address createdAddress = as.create(validAddress);

        // Verify that the address was saved successfully
        assertEquals(formatted, createdAddress);
    }

    @Test
    public void testInvalidCreateAddress() {
        var exception = assertThrows(IllegalArgumentException.class, () -> as.create(invalidAddress));
        assertAll(
                () -> assertNotNull(exception),
                () -> assertTrue(exception.getCause() instanceof AddressValidationException),
                () -> assertEquals(NONEXISTING, ((AddressValidationException) exception.getCause()).cause())
        );
    }

    @Test
    public void testGetAddress() {
        var formatted = Address.from(validAddress);
        Address createdAddress = as.create(validAddress);
        Address fetchedAddress = as.getById(1);
        assertEquals(createdAddress, fetchedAddress);
        assertEquals(formatted, fetchedAddress);
    }

    @Test
    public void testValidUpdateAddress() {
        as.create(validAddress);
        validAddress.setZipcode("03766");
        validAddress.setCity("lebanon");
        var formatted = Address.from(validAddress);
        Address updatedAddress = as.update(1,validAddress);

        assertEquals(formatted, updatedAddress);
    }

    @Test
    public void testInvalidUpdateAddress() {
        as.create(validAddress);
        validAddress.setZipcode("03766");
        validAddress.setCity("Nashua");
        var exception = assertThrows(IllegalArgumentException.class, () -> as.update(1,validAddress));
        assertAll(
                () -> assertNotNull(exception), // Ensure the exception is not null
                () -> assertTrue(exception.getCause() instanceof AddressValidationException), // Check the type of the cause
                () -> assertEquals(NONEXISTING, ((AddressValidationException) exception.getCause()).cause()) // Check the message of the exception
        );
    }

}
