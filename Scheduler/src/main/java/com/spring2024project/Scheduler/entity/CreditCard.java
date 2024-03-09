package com.spring2024project.Scheduler.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.spring2024project.Scheduler.customValidatorTags.ValidMonth;
import com.spring2024project.Scheduler.customValidatorTags.ValidYearRange;
import com.spring2024project.Scheduler.customValidatorTags.ZipCodeValidatorTag;
import com.spring2024project.Scheduler.exception.*;

import jakarta.persistence.*;
import lombok.*;

import static com.spring2024project.Scheduler.validatingMethods.CreditCardValidator.*;
import static com.spring2024project.Scheduler.exception.ValidationException.Cause.*;

import javax.validation.constraints.*;

import java.util.Objects;

/**
 * Entity class representing a Credit Card.
 * TODO: Customer has many-to-one relationship with Address, ensure functionality is demonstrated with CRUD operations
 */
@Entity
@Table(name = "credit_card")
@Getter
@Setter
@NoArgsConstructor
@Cacheable
@ToString(exclude = {"customer"})
@EqualsAndHashCode(exclude = {"id", "customer"})
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    @Pattern(regexp = "/^(?:4[0-9]{12}(?:[0-9]{3})?|[25][1-7][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})$/",
            message = "Credit Card number must match the following formats:\n" +
                        "1. Visa\n2. MasterCard\n3. American Express\n4. Diners Club\n5. Discover\n6. JCB")
    private String number;

    @Column
    @ValidMonth
    private int expMonth;

    @Column
    @ValidYearRange
    private int expYear;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_address_id")
    private Address billingAddress;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_customer_id")
    private Customer customer;

    private CreditCard(String number, int expMonth, int expYear, Address billingAddress, Customer customer) {
        this.number = number;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.billingAddress = billingAddress;
        this.customer = customer;
    }

    private CreditCard(int id, String number, int expMonth, int expYear, Address billingAddress, Customer customer) {
        this.id = id;
        this.number = number;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.billingAddress = billingAddress;
        this.customer = customer;
    }

    /**
     * Constructor for creating an empty CreditCard instance.
     * @return An empty CreditCard instance.
     */
    public static CreditCard emptyCreditCard() {
        return new CreditCard(0,"",0, 0, Address.emptyAddress(), Customer.defaultCustomer());
    }

    /**
     * Creates a new CreditCard instance from an existing one.
     * @param c The existing CreditCard instance.
     * @return A new CreditCard instance.
     * @throws IllegalArgumentException with
     */
    public static CreditCard from(CreditCard c) {
        return new CreditCard(
                correctCCNumberFormat(c.getNumber()),
                verifyMonth(c.getExpMonth()),
                verifyYearInRange(c.getExpYear(), 5),
                Address.from(c.getBillingAddress()),
                c.getCustomer());
    }

    /**
     * Checks the credit card to create a new CreditCard instance from has a valid address. This method should be used
     * when persisting a CreditCard during communication with the frontend and when attempting to store in the database.
     * @param c The existing CreditCard instance
     * @param v The ZipCodeValidator to use
     * @return A new Credit Card with a valid address
     */
    public static CreditCard checkedFrom(CreditCard c, ZipCodeValidatorTag v) {
        var newCC = from(c);
        if (!v.isValidAddress(Objects.requireNonNull(newCC).getBillingAddress())) {
            throw new IllegalArgumentException(
                    new AddressValidationException(
                            c.getBillingAddress(), NONEXISTING));
        }
        return newCC;
    }

    /**
     * Creates a new CreditCard instance from a deleted one.
     * @param c The deleted CreditCard instance.
     * @return A new CreditCard instance.
     */
    public static CreditCard fromDeleted(CreditCard c) {
        var checked = from(c);
        return new CreditCard(
                c.getId(),
                checked.getNumber(),
                checked.getExpMonth(),
                checked.getExpYear(),
                checked.getBillingAddress(),
                checked.getCustomer());
    }

}
