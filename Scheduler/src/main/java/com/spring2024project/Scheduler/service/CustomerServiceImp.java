package com.spring2024project.Scheduler.service;

import com.spring2024project.Scheduler.entity.Address;
import com.spring2024project.Scheduler.entity.CreditCard;
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
public class CustomerServiceImp implements CustomerService{
    private CustomerRepository cr;
    private final EntityManager em;

    @Autowired
    public CustomerServiceImp(CustomerRepository cr, EntityManager em) {
        this.cr = cr;
        this.em = em;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return (List<Customer>) cr.findAll();
    }

    @Override
    public Customer getCustomerByID(int id) {
        return cr.findById(id).orElse(defaultCustomer());
    }

    @Override
    public Customer createCustomer(String firstName, String lastName, String email, long phoneNumber, List<Address> addressList, List<CreditCard> creditCardList) {
        var newCustomer = Customer.of(firstName, lastName, email, phoneNumber, addressList, creditCardList);
        newCustomer.getAddressList().forEach(address -> address.setCustomer(newCustomer));
        newCustomer.getCreditCardList().forEach(creditCard -> creditCard.setCustomer(newCustomer));
        Customer mergedCustomer = em.merge(newCustomer);
        em.flush(); // optional, to ensure changes are immediately persisted
        return mergedCustomer;
    }

    @Override
    public Customer updateCustomer(int id, String firstName, String lastName, String email, long phoneNumber, List<Address> addressList, List<CreditCard> creditCardList) {
        Customer toUpdate = getCustomerByID(id);
        if (toUpdate.getId() == 0) {
            toUpdate.setFirstName(firstName);
            toUpdate.setLastName(lastName);
            toUpdate.setEmail(email);
            toUpdate.setPhoneNumber(phoneNumber);
            toUpdate.setAddressList(addressList);
            toUpdate.setCreditCardList(creditCardList);
        }
        return toUpdate;
    }

    @Override
    public Customer deleteCustomer(int id) {
        Customer toDelete = getCustomerByID(id);
        if (!toDelete.getFirstName().isEmpty()) {
            Customer save = Customer.from(toDelete);
            cr.deleteById(id);
            return save;
        }
        return toDelete;
    }
}
