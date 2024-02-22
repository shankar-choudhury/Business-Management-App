package com.spring2024project.Scheduler.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.spring2024project.Scheduler.validator.ValidState;
import jakarta.persistence.*;
import lombok.*;

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
    @Column
    private String street;
    @Column
    private String city;
    @Column
    @ValidState
    private String state;
    @Column
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

    public static Address defaultAddress() {
        return new Address();
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