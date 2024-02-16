package com.spring2024project.Scheduler.repository;

import com.spring2024project.Scheduler.entity.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer,Integer> {
}
