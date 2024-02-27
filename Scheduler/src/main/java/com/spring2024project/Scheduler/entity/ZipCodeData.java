package com.spring2024project.Scheduler.entity;

import static lombok.AccessLevel.*;

import static com.spring2024project.Scheduler.validatingMethods.StringValidator.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Entity class representing ZIP code data. This is not to be used as a type for other entities to use as a field representing
 * a feature on the database, but rather to be used to validate if an input zipcode is an existing one. This is checked
 * by attempting a mapping of the input zipcode to an instance of ZipCodeData, where the zipcode maps to the correct city
 * and state. ZipCodeData is also used to validate that the correct cities and states correspond to the zipcode.
 * @Author Shankar Choudhury
 */
@Entity
@Table(name = "zip_code_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class ZipCodeData {

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

    /**
     * Converts a comma-separated string of cities to a set of strings.
     *
     * @param cities The comma-separated string of cities.
     * @return A set containing the individual cities.
     */
    private static Set<String> toSet(String cities) {
        assert cities != null;
        return Arrays.stream(cities.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());
    }

    /**
     * Creates a new ZipCodeData instance with the provided attributes.
     * @param zip         The ZIP code.
     * @param primaryCity The primary city.
     * @param cities      The acceptable cities.
     * @param state       The state.
     * @param timezone    The timezone.
     * @return A new ZipCodeData instance.
     */
    public static ZipCodeData of(
            String zip,
            String primaryCity,
            String cities,
            String state,
            String timezone) {
        return new ZipCodeData(
                verifyNonNullEmptyOrBlank(zip),
                verifyNonNullEmptyOrBlank(primaryCity).trim(),
                toSet(cities),
                verifyNonNullEmptyOrBlank(state),
                verifyNonNullEmptyOrBlank(timezone));
    }

    /**
     * Creates an empty ZipCodeData instance.
     *
     * @return An empty ZipCodeData instance.
     */
    public static ZipCodeData emptyZipCodeData() {
        return new ZipCodeData("", "", Set.of(), "", "");
    }
}

