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
        em.persist(entity);
        return entity;
    }

    @Override
    public Customer update(int id, Customer entity) {
        Customer original = getById(id);
        if (original.getId() != 0) {
            entity.setId(id); // Ensure that the ID matches
            Customer updated = em.merge(entity);
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
