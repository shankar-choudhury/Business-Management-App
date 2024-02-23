package com.spring2024project.Scheduler.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.spring2024project.Scheduler.customValidatorTags.ValidState;
import com.spring2024project.Scheduler.customValidatorTags.ValidZipCode;
import com.spring2024project.Scheduler.customValidatorTags.ZipCodeValidator;

import static com.spring2024project.Scheduler.validatingMethods.GeneralValidator.verifyNonNull;
import static com.spring2024project.Scheduler.validatingMethods.StringValidator.*;
import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.*;
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
    @Pattern(regexp = "^\\d+\\w*$",
            message = "This matches building number patterns like \"123\", \"45A\", \"123B\", \"20-22\", \"1A\", \"9B\",")
    private String buildingNumber;

    @Column
    @Pattern(regexp = "\\d*[ ](?:[A-Za-z0-9.-]+[ ]?)+(?:Avenue|Lane|Road|Boulevard|Drive|Street|Ave|Dr|Rd|Blvd|Ln|St)\\.?",
            message = "This allows a fairly large range of street names")
    private String street;

    @Column
    @Pattern(regexp = "^[a-zA-Z\\u0080-\\u024F]+(?:. |-| |')*([1-9a-zA-Z\\u0080-\\u024F]+(?:. |-| |'))*[a-zA-Z\\u0080-\\u024F]*$",
            message = "This is an intentionally broad matching, as cities come in all types of names")
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
    private Customer customer = Customer.defaultCustomer();

    @JsonManagedReference
    @OneToMany(mappedBy = "creditcard", cascade = CascadeType.ALL)
    private CreditCard creditCard = CreditCard.emptyCreditCard();

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
        if (!validator.isMatchingCityAndState(
                Objects.requireNonNull(a).getZipcode(), a.getCity(), correctState(a.getState()))) {
            return emptyAddress();
        }
        return from(a);
    }

    public static Address from(Address a) {
        verifyNonNull(a);
        verifyNonNullEmptyOrBlank(a.getBuildingNumber(), a.getCity(), a.getStreet(), a.getState(), a.getZipcode());
        return new Address(
                correctBuildingNumFormat(a.getBuildingNumber()),
                correctStreetFormat(a.getStreet()),
                correctCityFormat(a.getCity()),
                correctState(a.getState()),
                a.getZipcode());
    }

    public static Address fromDeleted(Address a) {
        var checked = from(a);
        return new Address(checked.getId(),
                checked.getBuildingNumber(),
                checked.getStreet(),
                checked.getCity(),
                checked.getState(),
                checked.getZipcode());
    }
}