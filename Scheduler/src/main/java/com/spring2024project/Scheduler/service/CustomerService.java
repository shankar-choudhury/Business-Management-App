package com.spring2024project.Scheduler.service;

import com.spring2024project.Scheduler.entity.Address;
import com.spring2024project.Scheduler.entity.CreditCard;
import com.spring2024project.Scheduler.entity.Customer;

import java.util.List;

public interface CustomerService {

    List<Customer> getAllCustomers();
    Customer getCustomerByID(int id);
    Customer createCustomer(String firstName, String lastName, String email, long phoneNumber, List<Address> addressList, List<CreditCard> creditCardList);
    Customer updateCustomer(int id, String firstName, String lastName, String email,long phoneNumber, List<Address> addressList, List<CreditCard> creditCardList);
    Customer deleteCustomer(int id);

}
