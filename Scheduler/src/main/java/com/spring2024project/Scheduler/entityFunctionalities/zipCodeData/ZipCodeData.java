package com.spring2024project.Scheduler.entityFunctionalities.zipCodeData;

import static com.spring2024project.Scheduler.validatingMethods.GeneralValidator.getOrEmpty;
import static com.spring2024project.Scheduler.validatingMethods.StringValidator.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

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
@ToString
public final class ZipCodeData {

    @Id
    @Column(name = "zip_code")
    private String zip;

    @Column(name = "cities")
    private Set<String> acceptableCities = new HashSet<>();

    @Column
    private String state;


    /**
     * Creates a new ZipCodeData instance with the provided attributes.
     * @param zip         The ZIP code.
     * @param cities      The acceptable cities.
     * @param state       The state.
     * @return A new ZipCodeData instance.
     */
    public static ZipCodeData of(
            String zip,
            Set<String> cities,
            String state) {
        return new ZipCodeData(
                verifyNonNullEmptyOrBlank(zip),
                getOrEmpty(cities),
                verifyNonNullEmptyOrBlank(state));
    }

    /**
     * Creates an empty ZipCodeData instance.
     *
     * @return An empty ZipCodeData instance.
     */
    public static ZipCodeData emptyZipCodeData() {
        return new ZipCodeData("", Set.of(), "");
    }
}

