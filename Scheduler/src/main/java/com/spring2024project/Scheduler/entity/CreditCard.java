package com.spring2024project.Scheduler.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.spring2024project.Scheduler.customValidatorTags.ValidMonth;
import com.spring2024project.Scheduler.customValidatorTags.ValidYearRange;
import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.*;

import java.util.Objects;

import static com.spring2024project.Scheduler.validatingMethods.StringValidator.*;
import static com.spring2024project.Scheduler.validatingMethods.GeneralValidator.*;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address billingAddress;

    // Version field for optimistic locking
    @Version
    private Long version;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer = Customer.defaultCustomer();

    private CreditCard(String number, int expMonth, int expYear, Address billingAddress) {
        this.number = number;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.billingAddress = billingAddress;
    }

    private CreditCard(int id, String number, int expMonth, int expYear, Address billingAddress) {
        this.id = id;
        this.number = number;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.billingAddress = billingAddress;
    }

    /**
     * Constructor for creating an empty CreditCard instance.
     * @return An empty CreditCard instance.
     */
    public static CreditCard emptyCreditCard() {
        return new CreditCard(0,"",0, 0, Address.emptyAddress());
    }

    /**
     * Creates a new CreditCard instance from an existing one.
     * @param c The existing CreditCard instance.
     * @return A new CreditCard instance.
     */
    public static CreditCard from(CreditCard c) {
        return new CreditCard(
                correctCCNumberFormat(c.getNumber()),
                verifyMonth(c.getExpMonth()),
                verifyYearInRange(c.getExpYear(), 5),
                Objects.requireNonNull(c.getBillingAddress()));
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
                checked.getBillingAddress());
    }

}
