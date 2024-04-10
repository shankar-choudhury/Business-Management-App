package com.spring2024project.Scheduler.entityFunctionalities.people.customer;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer,Integer> {
    List<Customer> findByFirstNameAndLastName(String firstName, String lastName);
}
