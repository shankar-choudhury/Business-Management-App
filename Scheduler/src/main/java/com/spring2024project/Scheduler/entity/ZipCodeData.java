package com.spring2024project.Scheduler.entity;

import static com.spring2024project.Scheduler.validator.ValidatingMethods.*;
import static lombok.AccessLevel.*;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "zip_code_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = PRIVATE)
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

    private static Set<String> setOf(String cities) {
        assert cities != null;
        return Arrays.stream(cities.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());
    }

    public static ZipCodeData of(
            String zip,
            String primaryCity,
            String cities,
            String state,
            String timezone) {
        return new ZipCodeData(
                verifyNonNullEmptyOrBlank(zip),
                verifyNonNullEmptyOrBlank(primaryCity),
                setOf(Objects.requireNonNull(cities)),
                verifyNonNullEmptyOrBlank(state),
                verifyNonNullEmptyOrBlank(timezone));
    }



}
