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
    @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$",
            message = "Email must adhere to following format rules:\nThe local part (before the \"@\") can contain alphanumeric characters, underscores (_), and hyphens (-).\n" +
                    "1. The local part can have a dot (.) followed by more alphanumeric characters, underscores (_), and hyphens (-).\n This pattern can repeat zero or more times.\n" +
                    "2. The domain part cannot start with a hyphen (-).\n" +
                    "3. The domain part can contain alphanumeric characters, hyphens (-), and dots (.).\n" +
                    "4. The domain part can have a dot (.) followed by more alphanumeric characters and hyphens. This pattern can repeat zero or more times.\n" +
                    "5. The domain part must end with a dot (.) followed by at least two alphabetical characters (e.g., \".com\", \".org\").\n" +
                    "Some valid email examples are: \"example@example.com\", \"user123@example.co.uk\", and \"my.email@example-domain.com\"")
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
