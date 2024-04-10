package com.spring2024project.Scheduler.serviceTests;

import com.spring2024project.Scheduler.entityFunctionalities.address.Address;
import com.spring2024project.Scheduler.entityFunctionalities.creditcard.CreditCard;
import com.spring2024project.Scheduler.entityFunctionalities.people.customer.Customer;
import com.spring2024project.Scheduler.entityFunctionalities.address.AddressRepository;
import com.spring2024project.Scheduler.entityFunctionalities.creditcard.CreditCardRepository;
import com.spring2024project.Scheduler.entityFunctionalities.people.customer.CustomerRepository;
import com.spring2024project.Scheduler.entityFunctionalities.people.customer.CustomerService;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ComponentScan(basePackages = {"com.spring2024project.Scheduler"})
public class CustomerServiceTest {
    @Autowired
    private CustomerService cs;
    @Autowired
    private CustomerRepository cr;
    @Autowired
    private AddressRepository ar;
    @Autowired
    private CreditCardRepository ccr;
    private Customer c;
    private Address validAddress;
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

        cc = CreditCard.builder()
                .number("4400665614591913")
                .expMonth(4)
                .expYear(2025)
                .billingAddress(validAddress)
                .build();

        cr.deleteAll();
        ar.deleteAll();
        ccr.deleteAll();
    }

    @Test
    public void testCreate() {
        var customer = Customer.from(c);
        var created = cs.create(c);
        assertEquals(customer, created);
    }

    @Test
    public void testGetCustomer() {
        var customer = Customer.from(c);
        var created = cs.create(c);
        var fetched = cs.getById(created.getId());
        assertEquals(created, fetched);
        assertEquals(customer, fetched);
    }

    @Transactional
    @Test
    public void testValidUpdate() {
        var created = cs.create(c);
        //c.addAddress(validAddress);
        c.addCreditCard(cc);
        c.setPhoneNumber("6771412412");
        var updated = cs.update(created.getId(), c);
        assertEquals(c, updated);
        assertEquals(c.getCreditCardList().get(0).getCustomer().getPhoneNumber(), "6771412412");
        assertEquals(((List<CreditCard>) ccr.findAll()).get(0).getCustomer().getPhoneNumber(), "6771412412");
    }

    @Test
    public void testInvalidUpdate() {}
}
