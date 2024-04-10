package com.spring2024project.Scheduler.entityFunctionalities.people.customer;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.spring2024project.Scheduler.entityFunctionalities.address.Address;
import com.spring2024project.Scheduler.entityFunctionalities.creditcard.CreditCard;
import com.spring2024project.Scheduler.entityFunctionalities.generator.Generator;
import com.spring2024project.Scheduler.entityFunctionalities.job.Job;
import com.spring2024project.Scheduler.entityFunctionalities.people.Person;
import jakarta.persistence.*;
import lombok.*;

import static com.spring2024project.Scheduler.validatingMethods.StringValidator.*;
import static com.spring2024project.Scheduler.validatingMethods.PersonValidator.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"firstName", "lastName", "email", "phoneNumber"})
})
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public final class Customer extends Person {
    @JsonManagedReference(value = "customer-addresses")
    @OneToMany(
            mappedBy = "customer",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
            fetch = FetchType.LAZY
    )
    private List<Address> addressList;

    @JsonManagedReference(value = "customer-creditcards")
    @OneToMany(
            mappedBy = "customer",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private List<CreditCard> creditCardList = new ArrayList<>();

    @JsonManagedReference(value = "customer-jobs")
    @OneToMany(
            mappedBy = "customer",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
            fetch = FetchType.LAZY
    )
    private List<Job> jobs;

    @OneToMany(
            mappedBy = "customer",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
            fetch = FetchType.LAZY
    )
    private List<Generator> generators;

    public void setCreditCardList(List<CreditCard> creditCardList) {
        this.creditCardList.clear();
        if (Objects.nonNull(creditCardList))
            this.creditCardList.addAll(creditCardList);
    }

    private Customer(int id,
                     String firstName,
                     String lastName,
                     String email,
                     String phoneNumber,
                     List<Address> addressList,
                     List<CreditCard> creditCardList) {
        super(id, firstName, lastName, email, phoneNumber);
        this.addressList = addressList;
        this.creditCardList = creditCardList;
    }

    public void addAddress(Address address) {
        if (Objects.isNull(addressList))
            addressList = new ArrayList<>();
        addressList.add(address);
        address.setCustomer(this);
    }

    public void addCreditCard(CreditCard creditCard) {
        if (Objects.isNull(creditCardList))
            creditCardList = new ArrayList<>();
        creditCardList.add(creditCard);
        creditCard.setCustomer(this);
    }

    public static Customer defaultCustomer() {
        return new Customer(0,"", "", "", "", List.of(), List.of());
    }

    public static Customer from(Customer c) {
        verifyNonNullEmptyOrBlank(
                c.getFirstName(),
                c.getLastName(),
                c.getEmail(),
                c.getPhoneNumber());
        correctNameFormat(c.getFirstName());
        correctNameFormat(c.getLastName());
        correctEmailFormat(c.getEmail());
        correctPhoneNumberFormat(c.getPhoneNumber());

        c.setFirstName(reformatName(c.getFirstName()));
        c.setLastName(reformatName(c.getLastName()));
        return c;
    }

    public boolean equals(Object o) {
        if (Objects.nonNull(o) && o instanceof Customer c) {
            return c.getFirstName().equals(getFirstName()) &&
                    c.getLastName().equals(getLastName()) &&
                    c.getEmail().equals(getEmail()) &&
                    c.getPhoneNumber().equals(getPhoneNumber());
        }
        return false;
    }

}
