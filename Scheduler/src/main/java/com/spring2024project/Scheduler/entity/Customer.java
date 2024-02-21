package com.spring2024project.Scheduler.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

/**
 * This class represents a Customer, with their required details for business management
 * TODO: Create abstract classes Person and BaseEntity. Future classes representing people
 * TODO: like office and field employees will extend Person. All Entities will extend BaseEntity.
 *
 */
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"firstName", "lastName", "phoneNumber"})
})
@Getter
@Setter
public class Customer extends Person {
    @JsonManagedReference
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Address> addressList;
    @JsonManagedReference
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<CreditCard> creditCardList;

    public Customer() {};

    private Customer(String firstName,
                     String lastName,
                     String email,
                     String phoneNumber,
                     List<Address> addressList,
                     List<CreditCard> creditCardList) {
        super(firstName, lastName, email, phoneNumber);
        this.addressList = addressList;
        this.creditCardList = creditCardList;
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
        this.addressList.add(address);
        address.setCustomer(this);
    }

    public void addCreditCard(CreditCard creditCard) {
        this.creditCardList.add(creditCard);
        creditCard.setCustomer(this);
    }

    public static Customer defaultCustomer() {
        return new Customer(0,"", "", "", "", List.of(), List.of());
    }

    public static Customer from(Customer c) {
        return new Customer(
                c.getFirstName(),
                c.getLastName(),
                c.getEmail(),
                c.getPhoneNumber(),
                c.getAddressList(),
                c.getCreditCardList());
    }

    public static Customer fromDeleted(Customer c) {

        return new Customer(
                c.getId(),
                c.getFirstName(),
                c.getLastName(),
                c.getEmail(),
                c.getPhoneNumber(),
                c.getAddressList(),
                c.getCreditCardList());
    }

}
