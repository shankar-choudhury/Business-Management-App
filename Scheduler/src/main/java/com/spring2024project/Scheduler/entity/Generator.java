package com.spring2024project.Scheduler.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

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
    private Job associatedJob;

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
     * Constructor for Generator class with parameters including ID.
     * @param id The ID of the generator.
     * @param manufacturer The manufacturer of the generator.
     * @param kWSize The size of the generator in kW.
     * @param installInstructions Installation instructions for the generator.
     */
    private Generator(int id, String manufacturer, int kWSize, String installInstructions) {
        this.id = id;
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
        return new Generator(g.getManufacturer(), g.getKWSize());
    }

}
