package com.spring2024project.Scheduler.service;

import com.spring2024project.Scheduler.customValidatorTags.ZipCodeValidatorTag;
import com.spring2024project.Scheduler.entity.Address;
import com.spring2024project.Scheduler.entity.CreditCard;
import com.spring2024project.Scheduler.entity.Customer;
import com.spring2024project.Scheduler.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import static com.spring2024project.Scheduler.entity.Customer.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This service class provides functionality to perform CRUD operations on Customer entities.
 * @Author Shankar Choudhury
 */
@Service
@Transactional
public class CustomerService implements BaseService<Customer> {
    private final CustomerRepository cr;
    private final EntityManager em;
    private final AddressService as;
    private final CreditCardService ccs;
    private final ZipCodeValidatorTag zt;

    @Autowired
    public CustomerService(CustomerRepository cr,
                           EntityManager entityManager,
                           AddressService as,
                           CreditCardService ccs,
                           ZipCodeValidatorTag zt) {
        this.cr = cr;
        this.em = entityManager;
        this.as = as;
        this.ccs = ccs;
        this.zt = zt;
    }

    @Override
    public List<Customer> getAll() {
        return (List<Customer>) cr.findAll();
    }

    @Override
    public Customer getById(int id) {
        return cr.findById(id).orElse(defaultCustomer());
    }

    public List<Customer> findByFirstAndLastName(String firstName, String lastName) {
        return cr.findByFirstNameAndLastName(reformatName(firstName), reformatName(lastName));
    }

    private static String reformatName(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    // Test creating customer with existing address with customer reference being null
    @Override
    public Customer create(Customer entity) {
        try {
            var newCustomer = Customer.from(entity);

            // Check for existing addresses and associate them with the customer
            List<Address> existingAddresses = as.getAll();
            List<Address> customerAddresses = new ArrayList<>(); // New list to hold addresses associated with the customer

            if (Objects.nonNull(newCustomer.getAddressList())) {
                var customerAddressList = new ArrayList<>(newCustomer.getAddressList());
                for (Address address : customerAddressList) {
                    as.validateAndNormalizeAddress(address);
                    if (existingAddresses.contains(address)) {
                        var existingAddress = existingAddresses.get(existingAddresses.indexOf(address));
                        if (Objects.nonNull(existingAddress.getCustomer())) {
                            existingAddress.setCustomer(newCustomer);
                            customerAddresses.add(existingAddress); // Add existing address to the list of customer addresses
                        }
                    } else {
                        address.setCustomer(newCustomer);
                        customerAddresses.add(address); // Add new address to the list of customer addresses
                    }
                }
            }

            // Associate credit cards with billing addresses
            if (Objects.nonNull(newCustomer.getCreditCardList())) {
                for (CreditCard cc : newCustomer.getCreditCardList()) {
                    Address billingAddress = cc.getBillingAddress();
                    as.validateAndNormalizeAddress(billingAddress);
                    if (existingAddresses.contains(billingAddress)) {
                        Address existingAddress = existingAddresses.get(existingAddresses.indexOf(billingAddress));
                        existingAddress.setCustomer(newCustomer);
                        cc.setBillingAddress(existingAddress);
                    } else {
                        billingAddress.setCustomer(newCustomer);
                        customerAddresses.add(billingAddress); // Add new billing address to the list of customer addresses
                    }
                }
            }

            // Set the list of addresses associated with the customer
            newCustomer.setAddressList(customerAddresses);

            // Merge the customer entity
            em.merge(newCustomer);

            return newCustomer;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create customer", e);
        }
    }

    @Override
    public Customer update(int id, Customer entity) {
        Customer original = getById(id);
        if (original.getId() != 0) {
            entity.setId(id); // Ensure that the ID matches
            updateCustomerCreditCards(id, entity.getCreditCardList());
            updateCustomerAddresses(id, entity.getAddressList());
            return cr.save(Customer.from(entity));
        }
        return original;
    }

    public void updateCustomerCreditCards(int customerId, List<CreditCard> creditCards) {
        Customer customer = cr.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
        if (Objects.nonNull(creditCards)) {
            for (CreditCard creditCard : creditCards) {
                // Associate credit card with customer
                // Associate billing address with credit card
                Address billingAddress = creditCard.getBillingAddress();
                var existingAddresses = as.getAll();
                if (existingAddresses.contains(billingAddress)) {
                    creditCard.setBillingAddress(existingAddresses.get(existingAddresses.indexOf(billingAddress)));
                } else {
                    creditCard.setBillingAddress(Address.from(creditCard.getBillingAddress(), zt));
                    creditCard.getBillingAddress().setCustomer(customer);
                    em.merge(creditCard.getBillingAddress());
                }
                creditCard.setCustomer(customer);
            }
        }
    }

    public void updateCustomerAddresses(int customerId, List<Address> addresses) {
        Customer customer = cr.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
        if (Objects.nonNull(addresses)) {
            for (var address : addresses) {
                // Check address for proper format, and then associate with customer
                var checkedAddress = Address.from(address, zt);
                var existingAddresses = as.getAll();
                if (!existingAddresses.contains(address)) {
                    em.merge(address);
                }
                var customersAddresses = customer.getAddressList();
                // Ensure the address is also associated with the customer
                if (Objects.nonNull(customersAddresses) && !customersAddresses.contains(address)) {
                    customer.addAddress(address);
                }
                // Save credit card
                address.setCustomer(customer);
                em.merge(address);
            }
        }
    }

    @Override
    public Customer delete(int id) {
        Customer toDelete = getById(id);
        if (!toDelete.getFirstName().isEmpty()) {
            List<Address> existingAddresses = toDelete.getAddressList();
            if (Objects.nonNull(existingAddresses)) {
                for (Address address : existingAddresses) {
                    address.setCustomer(null);
                    em.merge(address);
                }
            }
            cr.deleteById(id);
        }
        return toDelete;
    }
}
