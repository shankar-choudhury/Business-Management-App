package com.spring2024project.Scheduler.controller;

import com.spring2024project.Scheduler.entity.Customer;
import com.spring2024project.Scheduler.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable int id) {
        Customer customer = customerService.getCustomerByID(id);
        return customer.getFirstName().isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer createdCustomer = customerService
                .createCustomer(
                        customer.getFirstName(),
                        customer.getLastName(),
                        customer.getEmail(),
                        customer.getPhoneNumber(),
                        customer.getAddressList(),
                        customer.getCreditCardList());
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable int id, @RequestBody Customer customer) {
        Customer updatedCustomer = customerService
                .updateCustomer(
                        id, customer.getFirstName(),
                        customer.getLastName(),
                        customer.getEmail(),
                        customer.getPhoneNumber(),
                        customer.getAddressList(),
                        customer.getCreditCardList());
        return updatedCustomer.getFirstName().isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable int id) {
        Customer deleted = customerService.deleteCustomer(id);
        return deleted.getFirstName().isEmpty() ?
                new ResponseEntity<>(deleted, HttpStatus.NOT_FOUND) : new ResponseEntity<>(deleted, HttpStatus.OK);
    }
}
