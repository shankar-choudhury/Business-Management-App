package com.spring2024project.Scheduler.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.spring2024project.Scheduler.validator.ValidMonth;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "creditcard")
@Getter
@Setter
@NoArgsConstructor
@Cacheable
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String number;
    @Column
    @ValidMonth
    private int expMonth;
    @Column
    private int expYear;
    @OneToOne
    private Address billingAddress;

    // Version field for optimistic locking
    @Version
    private Long version;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private CreditCard(String number, int expMonth, int expYear) {
        this.number = number;
        this.expMonth = expMonth;
        this.expYear = expYear;
    }

    private CreditCard(int id, String number, int expMonth, int expYear) {
        this.id = id;
        this.number = number;
        this.expMonth = expMonth;
        this.expYear = expYear;
    }

    public static CreditCard defaultCC() {
        return new CreditCard(0,"", 0, 0);
    }

    public static CreditCard from(CreditCard c) {
        return new CreditCard(c.getId(), c.getNumber(),
                c.getExpMonth(), c.getExpYear());
    }

}
