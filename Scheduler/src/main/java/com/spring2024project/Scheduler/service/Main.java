package com.spring2024project.Scheduler.service;
import com.spring2024project.Scheduler.entity.Customer;
import com.spring2024project.Scheduler.repository.CustomerRepository;
import com.spring2024project.Scheduler.service.CustomerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        // Start Spring Boot application and get application context
        ApplicationContext context = SpringApplication.run(Main.class, args);

        // Retrieve CustomerService bean from application context
        CustomerService customerService = context.getBean(CustomerService.class);

        // Create a new customer
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPhoneNumber("1234567890");

        // Save the new customer
        Customer savedCustomer = customerService.create(customer);
        System.out.println("Saved customer: " + savedCustomer);

        // Update the saved customer
        savedCustomer.setPhoneNumber("0987654321");
        Customer updatedCustomer = customerService.update(savedCustomer.getId(), savedCustomer);
        System.out.println("Updated customer: " + updatedCustomer);

        // Retrieve the updated customer from the database
        Customer retrievedCustomer = customerService.getById(updatedCustomer.getId());
        System.out.println("Retrieved customer: " + retrievedCustomer);

        // Delete the customer
        Customer deletedCustomer = customerService.delete(retrievedCustomer.getId());
        System.out.println("Deleted customer: " + deletedCustomer);

        // Check if the customer still exists after deletion
        Customer nonExistentCustomer = customerService.getById(deletedCustomer.getId());
        System.out.println("Non-existent customer: " + nonExistentCustomer);
    }
}
