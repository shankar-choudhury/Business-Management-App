package com.spring2024project.Scheduler.entity.employees;

import com.spring2024project.Scheduler.entity.Address;
import com.spring2024project.Scheduler.entity.Person;
import lombok.*;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@ToString(callSuper = true)
@Getter
@Setter
public abstract class Employee extends Person {
    private int salary;
    private Address homeAddress;
}
