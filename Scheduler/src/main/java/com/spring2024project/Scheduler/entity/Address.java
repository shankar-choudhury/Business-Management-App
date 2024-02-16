package com.spring2024project.Scheduler.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"buildingNumber", "street", "city", "state", "zipcode"})
})
@Getter
@Setter
@Cacheable
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private int buildingNumber;
    @Column
    private String street;
    @Column
    private String city;
    @Column
    private String state;
    @Column
    private int zipcode;

    // Version field for optimistic locking
    @Version
    private Long version;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Address() {
    }

    private Address(int buildingNumber, String street, String city, String state, int zipcode) {
        this.buildingNumber = buildingNumber;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
    }

    private Address(int id, int buildingNumber, String street, String city, String state, int zipcode) {
        this.id = id;
        this.buildingNumber = buildingNumber;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
    }

    public static Address defaultAddress() {
        return new Address(0, 0, "", "", "", 0);
    }

    public static Address of(int buildingNumber, String street, String city, String state, int zipcode) {
        return new Address(buildingNumber, street, city, state, zipcode);
    }

    public static Address from(Address a) {
        return new Address(a.getId(), a.getBuildingNumber(), a.getStreet(),
                a.getCity(), a.getState(), a.getZipcode());
    }
}