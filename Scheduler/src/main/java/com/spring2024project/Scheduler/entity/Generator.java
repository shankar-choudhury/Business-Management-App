package com.spring2024project.Scheduler.entity;

import jakarta.persistence.*;
import lombok.*;

import static com.spring2024project.Scheduler.validatingMethods.StringValidator.*;
import static com.spring2024project.Scheduler.validatingMethods.GeneralValidator.*;

/**
 * Entity class representing a generator. Generator will be associated with a Customer and Job.
 * @Author Shankar Choudhury
 */
@Entity
@Table(name = "generator")
@Getter
@Setter
@NoArgsConstructor
public final class Generator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String manufacturer;

    @Column
    private int kWSize;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    /**
     * Constructor for Generator class with parameters.
     * @param manufacturer The manufacturer of the generator.
     * @param kWSize The size of the generator in kW.
     */
    private Generator(String manufacturer, int kWSize) {
        this.manufacturer = manufacturer;
        this.kWSize = kWSize;
    }

    /**
     * Creates a default Generator instance.
     * @return A default Generator instance.
     */
    public static Generator defaultGenerator() {
        return new Generator("",0);
    }

    /**
     * Creates a new Generator instance from an existing one.
     * @param g The existing Generator instance.
     * @return A new Generator instance.
     */
    public static Generator from(Generator g) {
        return new Generator(
                verifyNonNullEmptyOrBlank(g.getManufacturer()),
                validInt(g.getKWSize(), kw -> kw > 0 && kw < 5000));
    }



}
