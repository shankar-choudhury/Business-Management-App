package com.spring2024project.Scheduler.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.spring2024project.Scheduler.customValidatorTags.ValidState;
import com.spring2024project.Scheduler.customValidatorTags.ValidZipCode;
import com.spring2024project.Scheduler.customValidatorTags.ZipCodeValidator;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"buildingNumber", "street", "city", "state", "zipcode"})
})
@Getter
@Setter
@NoArgsConstructor
@Cacheable
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String buildingNumber;

    @Column()
    private String street;

    @Column
    private String city;

    @Column
    @ValidState
    private String state;

    @Column
    @ValidZipCode
    private String zipcode;

    // Version field for optimistic locking
    @Version
    private Long version;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private Address(String buildingNumber, String street, String city, String state, String zipcode) {
        this.buildingNumber = buildingNumber;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
    }

    private Address(int id, String buildingNumber, String street, String city, String state, String zipcode) {
        this.id = id;
        this.buildingNumber = buildingNumber;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
    }

    public static Address emptyAddress() {
        return new Address();
    }

    public static Address from(Address a, ZipCodeValidator validator) {
        if (!validator.isMatchingCityAndState(Objects.requireNonNull(a).getZipcode(), a.getCity(), a.getState()))
            return emptyAddress();

        return new Address(
                a.getBuildingNumber(),
                a.getStreet(),
                a.getCity(),
                a.getState(),
                a.getZipcode());
    }

    public static Address from(Address a) {
        return new Address(
                a.getBuildingNumber(),
                a.getStreet(),
                a.getCity(),
                a.getState(),
                a.getZipcode());
    }

    public static Address fromDeleted(Address a) {
        return new Address(a.getId(), a.getBuildingNumber(), a.getStreet(),
                a.getCity(), a.getState(), a.getZipcode());
    }
}