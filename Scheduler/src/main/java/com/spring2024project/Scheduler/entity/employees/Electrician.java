package com.spring2024project.Scheduler.entity.employees;

import com.spring2024project.Scheduler.constantValues.ElectricianPermissions;
import com.spring2024project.Scheduler.entity.Address;
import com.spring2024project.Scheduler.entity.Job;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import static com.spring2024project.Scheduler.validatingMethods.PersonValidator.*;
import static com.spring2024project.Scheduler.validatingMethods.StringValidator.verifyNonNullEmptyOrBlank;
import static com.spring2024project.Scheduler.validatingMethods.EmployeeValidator.*;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"firstName", "lastName", "email", "phoneNumber"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class Electrician extends Employee {

    private Electrician(ElectricianPermissions level, List<Job> jobs,
                        int salary, Address homeAddress,
                        int id, String firstName, String lastName,
                        String email, String phoneNumber) {
        super(salary, homeAddress, id, firstName, lastName, email, phoneNumber);
        this.level = level;
        this.jobs = jobs;
    }

    // Validate and normalize address in Electrician service 
    public static Electrician from(Electrician e) {
        verifyNonNullEmptyOrBlank(
                e.getFirstName(),
                e.getLastName(),
                e.getEmail(),
                e.getPhoneNumber());
        correctNameFormat(e.getFirstName());
        correctNameFormat(e.getLastName());
        correctEmailFormat(e.getEmail());
        correctPhoneNumberFormat(e.getPhoneNumber());
        validSalary(e.getSalary());

        e.setFirstName(reformatName(e.getFirstName()));
        e.setLastName(reformatName(e.getLastName()));

        return e;
    }

    @Column
    private ElectricianPermissions level;

    @ManyToMany(mappedBy = "electricians")
    List<Job> jobs;
}
