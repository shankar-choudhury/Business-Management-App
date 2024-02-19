package com.spring2024project.Scheduler.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "generator")
@Getter
@Setter
public class Generator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String manufacturer;
    @Column
    private int kWSize;
    @Column
    private String installInstructions;

    public Generator(){};

    private Generator(String manufacturer, int kWSize, String installInstructions) {
        this.manufacturer = manufacturer;
        this.kWSize = kWSize;
        this.installInstructions = installInstructions;
    }

    private Generator(int id, String manufacturer, int kWSize, String installInstructions) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.kWSize = kWSize;
        this.installInstructions = installInstructions;
    }

    public static Generator defaultGenerator() {
        return new Generator("",0,"");
    }

    public static Generator from(Generator g) {
        return new Generator(g.getManufacturer(), g.getKWSize(), g.getInstallInstructions());
    }

}
