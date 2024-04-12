package com.spring2024project.Scheduler.entityFunctionalities.people;

import com.spring2024project.Scheduler.entityFunctionalities.address.Address;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@ToString(callSuper = true)
@Getter
@Setter
@MappedSuperclass
public abstract class Employee extends Person {
    private int salary;
    @OneToOne
    private Address homeAddress;

    protected Employee(int salary, Address homeAddress, int id, String firstName, String lastName, String email, String phoneNumber) {
        super(id, firstName, lastName, email, phoneNumber);
        this.salary = salary;
        this.homeAddress = homeAddress;
    }
}
