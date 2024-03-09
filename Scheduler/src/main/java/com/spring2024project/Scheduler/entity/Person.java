package com.spring2024project.Scheduler.entity;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;


/**
 * This class represents a skeletal implementation that holds common features and methods of entities representing people.
 * Future entities like OfficeEmployees, FieldTechnicians, Electricians, or Mechanics should extend this class.
 * @Author Shankar Choudhury
 * TODO: Add the following entities: OfficeEmployee, FieldTechnician, Electrician, and Mechanic
 */
@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @Pattern(regexp = "^[a-zA-Z]+$", message = "First name can only contain alphabetic characters")
    private String firstName;

    @Column
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name can only contain alphabetic characters")
    private String lastName;

    @Column
    @Email
    private String email;

    @Column
    @Pattern(regexp = "^(\\(?\\d{3}\\)?[-.\\s]?)?\\d{3}[-.\\s]?\\d{4}$",
            message = "Phone number can be in following format:\n" +
                    "1. (123) 456-7890\n" +
                    "2. (123)456-7890\n" +
                    "3. 123-456-7890\n" +
                    "4. 123.456.7890\n" +
                    "5. 123 456 7890\n" +
                    "6. 1234567890\n" +
                    "Or have as many spaces between the digits")
    private String phoneNumber;

    protected Person(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    protected Person(int id, String firstName, String lastName, String email, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
