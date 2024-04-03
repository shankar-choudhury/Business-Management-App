package com.spring2024project.Scheduler.service;

import com.spring2024project.Scheduler.customValidatorTags.ZipCodeValidatorTag;
import com.spring2024project.Scheduler.entity.Address;
import com.spring2024project.Scheduler.entity.CreditCard;
import com.spring2024project.Scheduler.entity.Customer;
import com.spring2024project.Scheduler.repository.AddressRepository;
import com.spring2024project.Scheduler.repository.CreditCardRepository;
import com.spring2024project.Scheduler.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import static com.spring2024project.Scheduler.entity.Customer.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This service class provides functionality to perform CRUD operations on Customer entities.
 * @Author Shankar Choudhury
 */
@Service
@Transactional
public class CustomerService implements BaseService<Customer> {
    private final CustomerRepository cr;
    private final EntityManager em;
    private final AddressService addressService;
    private final CreditCardService ccs;
    private final ZipCodeValidatorTag zt;

    @Autowired
    public CustomerService(CustomerRepository cr,
                           EntityManager entityManager,
                           AddressService addressService,
                           CreditCardService ccs,
                           ZipCodeValidatorTag zt) {
        this.cr = cr;
        this.em = entityManager;
        this.addressService = addressService;
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

    @Override
    public Customer create(Customer entity) {
        try {
            var newCustomer = Customer.from(entity);
            if (Objects.nonNull(newCustomer.getAddressList())) {
                for (Address address : newCustomer.getAddressList()) {
                    addressService.validateAndNormalizeAddress(address);
                }
            }
            if (Objects.nonNull(newCustomer.getCreditCardList())) {
                for (var cc : newCustomer.getCreditCardList()) {
                    addressService.validateAndNormalizeAddress(cc.getBillingAddress());
                }
            }
            assignCustomerToBillingAddress(newCustomer);
            em.persist(newCustomer);
            return newCustomer;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create customer", e);
        }
    }

    private void assignCustomerToBillingAddress(Customer newCustomer) {
        assert Objects.nonNull(newCustomer);
        var ccList = newCustomer.getCreditCardList();
        if (Objects.nonNull(ccList)) {
            for (var cc : ccList) {
                var existingAddresses = newCustomer.getAddressList();
                var billingAddress = cc.getBillingAddress();
                if (Objects.nonNull(existingAddresses) && existingAddresses.contains(billingAddress))
                    cc.setBillingAddress(existingAddresses.get(existingAddresses.indexOf(billingAddress)));
                else
                    cc.getBillingAddress().setCustomer(newCustomer);
            }
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
                var existingAddresses = addressService.getAll();
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
                var existingAddresses = addressService.getAll();
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
