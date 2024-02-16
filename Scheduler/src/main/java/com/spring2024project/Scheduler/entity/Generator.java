package com.spring2024project.Scheduler.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "generator")
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

    public int getId() {
        return id;
    }
    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getkWSize() {
        return kWSize;
    }

    public void setkWSize(int kWSize) {
        this.kWSize = kWSize;
    }

    public String getInstallInstructions() {
        return installInstructions;
    }

    public void setInstallInstructions(String installInstructions) {
        this.installInstructions = installInstructions;
    }

    public static Generator defaultGenerator() {
        return new Generator("",0,"");
    }

    public static Generator of(String manufacturer, int kWSize, String installInstructions) {
        return new Generator(manufacturer, kWSize, installInstructions);
    }

    public static Generator from(Generator g) {
        return new Generator(g.getManufacturer(), g.getkWSize(), g.getInstallInstructions());
    }

}
