package com.spring2024project.Scheduler.serviceTests;

import com.spring2024project.Scheduler.entityFunctionalities.address.Address;
import com.spring2024project.Scheduler.exception.AddressValidationException;
import com.spring2024project.Scheduler.entityFunctionalities.address.AddressRepository;
import com.spring2024project.Scheduler.customValidatorTags.ZipCodeValidatorTag;
import com.spring2024project.Scheduler.entityFunctionalities.address.AddressService;
import jakarta.transaction.Transactional;
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
        validAddress = Address.builder()
                .buildingNumber("1")
                .street("cedric st")
                .city("hanover")
                .state("new hampshire")
                .zipcode("03755")
                .build();

        invalidAddress = Address.builder()
                .buildingNumber("4")
                .street("cedric st")
                .city("Lebanon")
                .state("new hampshire")
                .zipcode("03755")
                .build();

        ar.deleteAll();
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

    @Transactional
    @Test
    public void testGetAddress() {
        var formatted = Address.from(validAddress);
        Address createdAddress = as.create(validAddress);

        Address fetchedAddress = as.getById(createdAddress.getId());

        assertEquals(createdAddress, fetchedAddress);
        assertEquals(formatted, fetchedAddress);
    }

    @Transactional
    @Test
    public void testValidUpdateAddress() {
        var created = as.create(validAddress);
        validAddress.setZipcode("03766");
        validAddress.setCity("lebanon");
        var formatted = Address.from(validAddress);
        Address updatedAddress = as.update(created.getId(),validAddress);

        assertEquals(formatted, updatedAddress);
    }

    @Test
    public void testInvalidUpdateAddress() {
        var created = as.create(validAddress);
        validAddress.setZipcode("03766");
        validAddress.setCity("Nashua");

        var exception = assertThrows(IllegalArgumentException.class, () -> as.update(created.getId(), validAddress));
        assertAll(
                () -> assertNotNull(exception), // Ensure the exception is not null
                () -> assertTrue(exception.getCause() instanceof AddressValidationException), // Check the type of the cause
                () -> assertEquals(NONEXISTING, ((AddressValidationException) exception.getCause()).cause()) // Check the message of the exception
        );
    }

    @Test
    public void testDeleteAddress() {
        var created = as.create(validAddress);
        var deleted = as.delete(created.getId());
        assertEquals(created, deleted);
    }

}
