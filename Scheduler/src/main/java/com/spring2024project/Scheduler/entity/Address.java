package com.spring2024project.Scheduler.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.spring2024project.Scheduler.customValidatorTags.ValidState;
import com.spring2024project.Scheduler.customValidatorTags.ValidZipCode;
import com.spring2024project.Scheduler.customValidatorTags.ZipCodeValidatorTag;

import static com.spring2024project.Scheduler.validatingMethods.GeneralValidator.verifyNonNull;
import static com.spring2024project.Scheduler.validatingMethods.StringValidator.*;

import com.spring2024project.Scheduler.exception.AddressValidationException;
import com.spring2024project.Scheduler.exception.ValidationException;
import com.spring2024project.Scheduler.validatingMethods.AddressValidator;
import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.*;
import java.util.List;
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
    @OneToMany(mappedBy = "billingAddress", cascade = CascadeType.ALL)
    private List<CreditCard> creditCardList;

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

    /**
     * Constructor for creating an empty Address instance.
     * @return An empty Address instance.
     */
    public static Address emptyAddress() {
        return new Address();
    }

    /**
     * Creates a new Address instance from an existing one using a ZipCodeValidator. This method should be used primarily
     * to check that the zipcode of an address is a correct one, i.e it exists and it contains a mapping to a city and state
     * that corresponds with this Address's city and state.
     * // TODO: Add functionality so that when creating an address, a temporary address is created, and then is checked for
     * // TODO: correct zipcode-city-state mapping. If correct, add address to database. If not correct, save fields back
     * // TODO: into text boxes of window, destroy address, and prompt user to try again with a correct zipcode-city-state mapping.
     * @param a The existing Address instance.
     * @param validator The ZipCodeValidator to use.
     * @return A new Address instance.
     */
    public static Address from(Address a, ZipCodeValidatorTag validator) {
        if (!validator.isValidAddress(Objects.requireNonNull(a))) {
            throw new IllegalArgumentException(
                    new AddressValidationException(
                            a, ValidationException.Cause.NONEXISTING));
        }
        return from(a);
    }

    /**
     * Creates a new Address instance from an existing one.
     * @param a The existing Address instance.
     * @return A new Address instance.
     */
    public static Address from(Address a) {
        verifyNonNull(a);
        verifyNonNullEmptyOrBlank(a.getBuildingNumber(), a.getCity(), a.getStreet(), a.getState(), a.getZipcode());
        return new Address(
                AddressValidator.correctBuildingNumFormat(a.getBuildingNumber()),
                AddressValidator.correctStreetFormat(a.getStreet()),
                AddressValidator.correctCityFormat(a.getCity()),
                AddressValidator.correctState(AddressValidator.correctStateFormat(a.getState())),
                a.getZipcode());
    }

    /**
     * Creates a new Address instance from a deleted one. The intention of this method is to return a copy of the deleted
     * Address for the sake of reference.
     * TODO: implement this so that when a user deletes an address, it can be saved on a stack to recall.
     * @param a The deleted Address instance.
     * @return A new Address instance.
     */
    public static Address fromDeleted(Address a) {
        //var checked = from(a);
        return new Address(a.getId(),
                a.getBuildingNumber(),
                a.getStreet(),
                a.getCity(),
                a.getState(),
                a.getZipcode());
    }

    public static void main(String[] args) {

    }
}