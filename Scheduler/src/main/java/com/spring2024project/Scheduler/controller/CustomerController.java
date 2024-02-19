package com.spring2024project.Scheduler.controller;

import com.spring2024project.Scheduler.entity.Customer;
import com.spring2024project.Scheduler.service.BaseService;
import com.spring2024project.Scheduler.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController implements BaseController<Customer>{

    private final BaseService<Customer> customerService;

    @Autowired
    public CustomerController(BaseService<Customer> customerService) {
        this.customerService = customerService;
    }

    @Override
    @GetMapping
    public ResponseEntity<List<Customer>> getAll() {
        List<Customer> customers = customerService.getAll();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getById(@PathVariable int id) {
        Customer customer = customerService.getById(id);
        return customer.getFirstName().isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @Override
    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody Customer customer) {
        Customer createdCustomer = customerService.create(customer);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable int id, @RequestBody Customer customer) {
        Customer updatedCustomer = customerService.update(id, customer);
        return updatedCustomer.getFirstName().isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> delete(@PathVariable int id) {
        Customer deleted = customerService.delete(id);
        return deleted.getFirstName().isEmpty() ?
                new ResponseEntity<>(deleted, HttpStatus.NOT_FOUND) : new ResponseEntity<>(deleted, HttpStatus.OK);
    }
}
