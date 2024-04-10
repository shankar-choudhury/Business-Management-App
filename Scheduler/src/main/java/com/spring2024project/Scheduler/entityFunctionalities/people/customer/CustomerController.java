package com.spring2024project.Scheduler.entityFunctionalities.people.customer;

import com.spring2024project.Scheduler.entityFunctionalities.BaseController;
import com.spring2024project.Scheduler.entityFunctionalities.creditcard.CreditCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing Customer entities.
 * @Author Shankar Choudhury
 */
@RestController
@RequestMapping("/customers")
public class CustomerController implements BaseController<Customer> {

    private final CustomerService customerService;

    /**
     * Constructor for CustomerController.
     * @param customerService The service responsible for handling Customer-related operations.
     */
    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Retrieve all customers.
     * @return ResponseEntity containing a list of Customer objects.
     */
    @Override
    @GetMapping
    public ResponseEntity<List<Customer>> getAll() {
        List<Customer> customers = customerService.getAll();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    /**
     * Retrieve a customer by its ID.
     * @param id The ID of the customer to retrieve.
     * @return ResponseEntity containing the retrieved Customer object.
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getById(@PathVariable int id) {
        Customer customer = customerService.getById(id);
        return customer.getFirstName().isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Customer>> getByFirstAndLastName(@RequestParam String firstName, @RequestParam String lastName) {
        List<Customer> customers = customerService.findByFirstAndLastName(firstName, lastName);
        return customers.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(customers, HttpStatus.OK);
    }

    /**
     * Create a new customer.
     * @param customer The Customer object to create.
     * @return ResponseEntity containing the created Customer object.
     */
    @Override
    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody Customer customer) {
        Customer createdCustomer = customerService.create(customer);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    /**
     * Update an existing customer.
     * @param id The ID of the customer to update.
     * @param customer The updated Customer object.
     * @return ResponseEntity containing the updated Customer object.
     */
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable int id, @RequestBody Customer customer) {
        Customer updatedCustomer = customerService.update(id, customer);
        return updatedCustomer.getFirstName().isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    /**
     * Create a new credit card for the specified customer.
     * @param id The ID of the customer to associate the credit card with.
     * @param creditCards The CreditCard object to create.
     * @return ResponseEntity containing the created CreditCard object.
     */
    @PutMapping("/{id}/credit-cards")
    public ResponseEntity<String> updateCustomerCreditCards(@PathVariable int id, @RequestBody List<CreditCard> creditCards) {
        try {
            customerService.updateCustomerCreditCards(id, creditCards);
            return ResponseEntity.ok("Credit cards updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating credit cards: " + e.getMessage());
        }
    }

    /**
     * Delete a customer by its ID.
     * @param id The ID of the customer to delete.
     * @return ResponseEntity containing the deleted Customer object.
     */
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> delete(@PathVariable int id) {
        Customer deleted = customerService.delete(id);
        return deleted.getFirstName().isEmpty() ?
                new ResponseEntity<>(deleted, HttpStatus.NOT_FOUND) : new ResponseEntity<>(deleted, HttpStatus.OK);
    }
}
