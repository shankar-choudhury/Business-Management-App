package com.spring2024project.Scheduler.entity.employees;

import com.spring2024project.Scheduler.entity.Address;
import com.spring2024project.Scheduler.entity.Person;
import lombok.*;

@NoArgsConstructor
@ToString(callSuper = true)
@Getter
@Setter
public abstract class Employee extends Person {
    private int salary;
    private Address homeAddress;

    protected Employee(int salary, Address homeAddress, int id, String firstName, String lastName, String email, String phoneNumber) {
        super(id, firstName, lastName, email, phoneNumber);
        this.salary = salary;
        this.homeAddress = homeAddress;
    }
}
