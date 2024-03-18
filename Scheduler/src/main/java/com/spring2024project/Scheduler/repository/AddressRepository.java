package com.spring2024project.Scheduler.repository;

import com.spring2024project.Scheduler.entity.Address;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface AddressRepository extends CrudRepository<Address, Integer> {
}
