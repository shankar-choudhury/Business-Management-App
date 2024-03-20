package com.spring2024project.Scheduler.service;

import com.spring2024project.Scheduler.entity.Address;
import com.spring2024project.Scheduler.entity.CreditCard;
import com.spring2024project.Scheduler.entity.Customer;
import com.spring2024project.Scheduler.repository.AddressRepository;
import com.spring2024project.Scheduler.repository.CreditCardRepository;
import com.spring2024project.Scheduler.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final AddressRepository ar;
    private final CreditCardRepository ccr;
    private final EntityManager em;

    @Autowired
    public CustomerService(CustomerRepository cr, AddressRepository ar, CreditCardRepository ccr, EntityManager entityManager) {
        this.cr = cr;
        this.ar = ar;
        this.ccr = ccr;
        this.em = entityManager;
    }

    @Override
    public List<Customer> getAll() {
        return (List<Customer>) cr.findAll();
    }

    @Override
    public Customer getById(int id) {
        return cr.findById(id).orElse(defaultCustomer());
    }

    @Override
    public Customer create(Customer entity) {
        try {
            var newCustomer = Customer.from(entity);
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
            Customer updated = em.merge(Customer.from(entity));
            updateAssociatedEntities(original, updated);
            return updated;
        }
        return original;
    }

    private void updateAssociatedEntities(Customer oldCopy, Customer newCopy) {
        if (Objects.nonNull(oldCopy.getAddressList())) {
            for (Address address : oldCopy.getAddressList()) {
                if (Objects.nonNull(address.getCustomer())) {
                    address.setCustomer(newCopy);
                    em.merge(address);
                }
            }
        }
        if (Objects.nonNull(oldCopy.getCreditCardList())) {
            for (CreditCard creditCard : oldCopy.getCreditCardList()) {
                creditCard.setCustomer(newCopy);
                em.merge(creditCard);
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
