package com.spring2024project.Scheduler.service;

import com.spring2024project.Scheduler.entity.Customer;
import com.spring2024project.Scheduler.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.spring2024project.Scheduler.entity.Customer.*;

import java.util.List;

/**
 * This service class provides functionality to perform CRUD operations on Customer entities.
 * @Author Shankar Choudhury
 */
@Service
@Transactional
public class CustomerService implements BaseService<Customer> {
    private CustomerRepository cr;
    private final EntityManager em;

    /**
     * Constructs a CustomerService instance with the given CustomerRepository and EntityManager.
     * @param cr The CustomerRepository to be used by the service.
     * @param em The EntityManager to be used by the service.
     */
    @Autowired
    public CustomerService(CustomerRepository cr, EntityManager em) {
        this.cr = cr;
        this.em = em;
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
        newCustomer.getAddressList().forEach(address -> address.setCustomer(newCustomer));
        newCustomer.getCreditCardList().forEach(creditCard -> creditCard.setCustomer(newCustomer));
        Customer mergedCustomer = em.merge(newCustomer);
        em.flush(); // optional, to ensure changes are immediately persisted
        return mergedCustomer;
    }

    /**
     * Updates an existing customer in the database.
     * @param id The ID of the customer to update.
     * @param entity The updated customer entity.
     * @return The updated customer.
     */
    @Override
    public Customer update(int id, Customer entity) {
        Customer toUpdate = getById(id);
        if (toUpdate.getId() != 0) {
            toUpdate = from(entity);
            return cr.save(toUpdate);
        }
        return toUpdate;
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
            cr.deleteById(id);
            return save;
        }
        return toDelete;
    }
}
