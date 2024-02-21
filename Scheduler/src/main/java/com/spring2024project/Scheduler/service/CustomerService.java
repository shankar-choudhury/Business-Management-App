package com.spring2024project.Scheduler.service;

import com.spring2024project.Scheduler.entity.Customer;
import com.spring2024project.Scheduler.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.spring2024project.Scheduler.entity.Customer.*;

import java.util.List;

@Service
@Transactional
public class CustomerService implements BaseService<Customer> {
    private CustomerRepository cr;
    private final EntityManager em;

    @Autowired
    public CustomerService(CustomerRepository cr, EntityManager em) {
        this.cr = cr;
        this.em = em;
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
        var newCustomer = Customer.from(entity);
        newCustomer.getAddressList().forEach(address -> address.setCustomer(newCustomer));
        newCustomer.getCreditCardList().forEach(creditCard -> creditCard.setCustomer(newCustomer));
        Customer mergedCustomer = em.merge(newCustomer);
        em.flush(); // optional, to ensure changes are immediately persisted
        return mergedCustomer;
    }

    @Override
    public Customer update(int id, Customer entity) {
        Customer toUpdate = getById(id);
        if (toUpdate.getId() != 0) {
            toUpdate = from(entity);
            return cr.save(toUpdate);
        }
        return toUpdate;
    }

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
