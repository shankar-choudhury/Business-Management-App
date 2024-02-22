package com.spring2024project.Scheduler.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "zip_code_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ZipCodeData {

    @Id
    @Column(name = "zip_code")
    private String zip;
    @Column(name = "primary_city", length = 50, nullable = false)
    private String primaryCity;
    @Column(name = "acceptable_cities")
    private Set<String> acceptableCities;
    @Column
    private String state;
    @Column
    private String timezone;

    public void setAcceptableCities(String cities) {
        this.acceptableCities = Arrays.stream(cities.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());
    }
}
