package com.spring2024project.Scheduler.service;

import com.spring2024project.Scheduler.entity.Address;
import com.spring2024project.Scheduler.entity.CreditCard;
import com.spring2024project.Scheduler.entity.Customer;
import com.spring2024project.Scheduler.repository.AddressRepository;
import com.spring2024project.Scheduler.repository.CreditCardRepository;
import com.spring2024project.Scheduler.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Service;

import static com.spring2024project.Scheduler.entity.Customer.*;

import java.util.AbstractMap;
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

    /**
     * Constructs a CustomerService instance with the given CustomerRepository and EntityManager.
     * @param cr The CustomerRepository to be used by the service.
     */
    @Autowired
    public CustomerService(CustomerRepository cr, AddressRepository ar, CreditCardRepository ccr) {
        this.cr = cr;
        this.ar = ar;
        this.ccr = ccr;
    }

    /**
     * Retrieves all customers from the database.
     * @return A list of all customers.
     */
    @Override
    public List<Customer> getAll() {
        return (List<Customer>) cr.findAll();
    }

    /**
     * Retrieves a customer by its ID from the database.
     * @param id The ID of the customer to retrieve.
     * @return The customer with the specified ID, or a default customer if not found.
     */
    @Override
    public Customer getById(int id) {
        return cr.findById(id).orElse(defaultCustomer());
    }

    /**
     * Creates a new customer in the database.
     * @param entity The customer entity to create.
     * @return The created customer.
     */
    @Override
    public Customer create(Customer entity) {
        var newCustomer = Customer.from(entity);
        return cr.save(newCustomer);
    }

    /**
     * Updates an existing customer in the database.
     * @param id The ID of the customer to update.
     * @param entity The updated customer entity.
     * @return The updated customer.
     */
    @Override
    public Customer update(int id, Customer entity) {
        Customer original = getById(id);
        if (original.getId() != 0) {
            var updated = Customer.from(entity);
            var oldCopy = delete(id);
            var newCopy = cr.save(updated);
            updateAssociatedEntities(oldCopy, newCopy);
            return newCopy;
        }
        return original;
    }

    private void updateAssociatedEntities(Customer oldCopy, Customer newCopy) {
        if (Objects.nonNull(oldCopy.getAddressList())) {
            for (Address address : oldCopy.getAddressList()) {
                if (Objects.nonNull(address.getCustomer())) {
                    address.setCustomer(newCopy);
                    ar.save(address);
                }
            }
        }
        if (Objects.nonNull(oldCopy.getCreditCardList())) {
            for (CreditCard creditCard : oldCopy.getCreditCardList()) {
                creditCard.setCustomer(newCopy);
                ccr.save(creditCard);
            }
        }
    }



    /**
     * Deletes a customer from the database.
     * @param id The ID of the customer to delete.
     * @return The deleted customer, or a default customer if not found.
     */
    @Override
    public Customer delete(int id) {
        Customer toDelete = getById(id);
        if (!toDelete.getFirstName().isEmpty()) {
            Customer save = Customer.fromDeleted(toDelete);
            var existingAddresses = save.getAddressList();
            if (Objects.nonNull(existingAddresses))
                existingAddresses.forEach(address -> address.setCustomer(null));
            cr.deleteById(id);
            return save;
        }
        return toDelete;
    }
}
