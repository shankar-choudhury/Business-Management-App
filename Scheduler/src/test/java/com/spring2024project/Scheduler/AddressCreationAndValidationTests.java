package com.spring2024project.Scheduler;

import com.spring2024project.Scheduler.customValidatorTags.ZipCodeValidatorTag;
import com.spring2024project.Scheduler.entity.Address;
import com.spring2024project.Scheduler.exception.AddressValidationException;
import com.spring2024project.Scheduler.exception.StateValidationException;
import com.spring2024project.Scheduler.exception.StringValidationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SchedulerApplication.class)
public class AddressCreationAndValidationTests {

    @Autowired
    private ZipCodeValidatorTag zipVal;
    private Address testAddress;



    @Before
    public void setUp() {
        testAddress = new Address();
        testAddress.setId(1);
        testAddress.setBuildingNumber("35");
        testAddress.setStreet("School St");
        testAddress.setCity("Hanover");
        testAddress.setState("NH");
        testAddress.setZipcode("03755");


    }

    @Test
    public void testValidAddressCreation() {
        try {
            Address.from(testAddress);
        } catch (IllegalArgumentException i) {
            if (i.getCause() instanceof StringValidationException s) {
                System.out.println(s.cause());
                System.out.println(s.getBadString());
                System.out.println(s.explanation());
            }
            else if (i.getCause() instanceof StateValidationException st) {
                System.out.println(st.cause());
                System.out.println(st.getNonExistingState());
                System.out.println(st.explanation());
            }
            fail("Address elements should have correct format");
        } catch (Exception e) {
            fail("No other exception should have been thrown");
        }

        try {
            Address.from(testAddress, zipVal);
        } catch (IllegalArgumentException i) {
            if (i.getCause() instanceof AddressValidationException a) {
                System.out.println(a.cause());
                System.out.println(a.getInvalidAddress());
                System.out.println(a.explanation());
            }
        }

    }

}
