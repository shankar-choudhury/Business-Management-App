package com.spring2024project.Scheduler.entityFunctionalities.address;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.spring2024project.Scheduler.customValidatorTags.ValidState;
import com.spring2024project.Scheduler.customValidatorTags.ValidZipCode;
import com.spring2024project.Scheduler.customValidatorTags.ZipCodeValidatorTag;

import static com.spring2024project.Scheduler.validatingMethods.GeneralValidator.verifyNonNull;
import static com.spring2024project.Scheduler.validatingMethods.StringValidator.*;
import static com.spring2024project.Scheduler.validatingMethods.AddressValidator.*;

import com.spring2024project.Scheduler.entityFunctionalities.creditcard.CreditCard;
import com.spring2024project.Scheduler.entityFunctionalities.people.customer.Customer;
import com.spring2024project.Scheduler.exception.AddressValidationException;
import com.spring2024project.Scheduler.exception.ValidationException;
import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.*;
import java.util.List;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"buildingNumber", "street", "city", "state", "zipcode"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Cacheable
@ToString(exclude = {"creditCardList", "customer"})
@EqualsAndHashCode(exclude = {"id", "customer","creditCardList"})
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @Pattern(regexp = "^\\d+\\w*$",
            message = "This matches building number patterns like \"123\", \"45A\", \"123B\", \"20-22\", \"1A\", \"9B\",")
    private String buildingNumber;

    @Column
    @Pattern(regexp = "^(?!(\\S+\\s+){10})(?!.*\\b\\w{25,}\\b)[\\w',.\"{}]{1,25}(?:\\s+[\\w',.\"{}]{1,25}){0,9}$",
            message = "This allows a fairly large range of street names")
    private String street;

    @Column
    private String city;

    @Column
    @ValidState
    private String state;

    @Column
    @ValidZipCode
    private String zipcode;

    @JsonBackReference(value = "customer-addresses")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @JsonManagedReference(value = "address-creditcards")
    @OneToMany(
            mappedBy = "billingAddress",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
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
     * @param a The existing Address instance.
     * @param validator The ZipCodeValidator to use.
     * @return A new Address instance.
     */
    public static Address from(Address a, ZipCodeValidatorTag validator) {
        var formattedAddress = Address.from(a);
        if (!validator.isValidAddress(formattedAddress)) {
            throw new IllegalArgumentException(
                    new AddressValidationException(
                            a, ValidationException.Cause.NONEXISTING));
        }
        return formattedAddress;
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
                correctBuildingNumFormat(a.getBuildingNumber()),
                correctStreetFormat(a.getStreet()),
                formatString(correctCityFormat(a.getCity())),
                correctState(correctStateFormat(a.getState())),
                correctZipCodeFormat(a.getZipcode()));
    }

    /**
     * Creates a new Address instance from a deleted one. The intention of this method is to return a copy of the deleted
     * Address for the sake of reference.
     * TODO: implement this so that when a user deletes an address, it can be saved on a stack to recall.
     * @param a The deleted Address instance.
     * @return A new Address instance.
     */
    public static Address fromDeleted(Address a) {
        var checked = from(a);
        return new Address(a.getId(),
                checked.getBuildingNumber(),
                checked.getStreet(),
                checked.getCity(),
                checked.getState(),
                checked.getZipcode());
    }

}