package com.spring2024project.Scheduler.entityFunctionalities.people.electrician;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ElectricianRepository extends CrudRepository<Electrician, Integer> {

    List<Electrician> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);
}
