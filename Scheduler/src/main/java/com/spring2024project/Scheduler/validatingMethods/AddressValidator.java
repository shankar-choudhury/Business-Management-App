package com.spring2024project.Scheduler.validatingMethods;

import com.spring2024project.Scheduler.constantValues.State;
import com.spring2024project.Scheduler.exception.StateValidationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Function;

import static com.spring2024project.Scheduler.exception.ValidationException.Cause.*;
import static com.spring2024project.Scheduler.validatingMethods.StringValidator.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddressValidator {
    /**
     * Enum containing regex patterns for address validation.
     */
    public enum AddressValidationPattern {
        VALID_STREET_PATTERN("(?i)\\b(?:(?:[0-9]+(?:st|nd|rd|th) )?(?:[a-zA-Z]+|(?:(?:[a-zA-Z]+\\s+)?[0-9]+(?:st|nd|rd|th)?))\\s+(?:Street|St\\.?|Road|Rd\\.?|Avenue|Ave\\.?|Boulevard|Blvd\\.?|Lane|Ln\\.?|Drive|Dr\\.?|Court|Ct\\.?|Place|Pl\\.?|Square|Sq\\.?|Circle|Cir\\.?|Way|Terrace|Terr\\.?|Trail|Tr\\.?|Parkway|Pkwy\\.?|Highway|Hwy\\.?))\\b",
                "Matches a valid street name with optional street suffixes. Examples: '123 Main St', '45 Elm Avenue', '1001 Oak Blvd'"),
        VALID_CITY_PATTERN("^[a-zA-Z\\u0080-\\u024F]+(?:. |-| |')*([1-9a-zA-Z\\u0080-\\u024F]+(?:. |-| |'))*[a-zA-Z\\u0080-\\u024F]*$",
                "Matches a valid city name. Examples: 'New York', 'Los Angeles', 'San Francisco'"),
        VALID_STATE_PATTERN("^[a-zA-Z ]+$",
                "Matches a valid state abbreviation or full name. Examples: 'CA', 'New York', 'TX'"),
        VALID_BUILDING_NUM_PATTERN("^\\d+\\w*$",
                "Matches a valid building number. Examples: '123', '100A', '22B'"),
        VALID_ZIPCODE_PATTERN("^\\d{5}$",
        "Matches a valid zip code format. Examples: '0000', '12345', '99999'");

        private final String regex;
        private final String description;

        AddressValidationPattern(String regex, String description) {
            this.regex = regex;
            this.description = description;
        }

        /**
         * Retrieves the regex pattern.
         * @return The regex pattern.
         */
        public String getRegex() {
            return regex;
        }

        /**
         * Retrieves the description of the regex pattern.
         * @return The description.
         */
        public String getDescription() {
            return description;
        }
    }

    /**
     * Validates the format of the given building number.
     * @param buildingNumber The building number to validate.
     * @return The input building number if it is in a valid format.
     * @throws IllegalArgumentException if the building number format is incorrect.
     */
    public static String correctBuildingNumFormat(String buildingNumber) {
        return validateString(buildingNumber, string -> string.matches(AddressValidationPattern.VALID_BUILDING_NUM_PATTERN.getRegex()), FORMAT);
    }

    /**
     * Validates the format of the given street name.
     * @param street The street name to validate.
     * @return The input street name if it is in a valid format.
     * @throws IllegalArgumentException if the street name format is incorrect.
     */
    public static String correctStreetFormat(String street) {
        return validateString(street, string -> string.matches(AddressValidationPattern.VALID_STREET_PATTERN.getRegex()), FORMAT);
    }

    /**
     * Validates the format of the given city name.
     * @param city The city name to validate.
     * @return The input city name if it is in a valid format.
     * @throws IllegalArgumentException if the city name format is incorrect.
     */
    public static String correctCityFormat(String city) {
        return validateString(city, string -> string.matches(AddressValidationPattern.VALID_CITY_PATTERN.getRegex()), FORMAT);
    }

    /**
     * Validates the format of the given state name.
     * @param state The state name to validate.
     * @return The input state name if it is in a valid format.
     * @throws IllegalArgumentException if the state name format is incorrect.
     */
    public static String correctStateFormat(String state) {
        return validateString(state, string -> string.matches(AddressValidationPattern.VALID_STATE_PATTERN.getRegex()), FORMAT);
    }

    /**
     * Validates the given state abbreviation.
     * @param state The state abbreviation to validate.
     * @return The input state abbreviation if it is valid.
     * @throws IllegalArgumentException if the state abbreviation is not valid.
     */
    public static String correctState(String state) {
        return validateState(state);
    }

    /**
     * Validates the format of the given zip code.
     * @param zipCode The zipcode to validate.
     * @return The input state name if it is in a valid format.
     * @throws IllegalArgumentException if the state name format is incorrect.
     */
    public static String correctZipCodeFormat(String zipCode) {
        return validateString(zipCode, string -> string.matches(AddressValidationPattern.VALID_ZIPCODE_PATTERN.getRegex()), FORMAT);
    }

    private static String validateState(String stateToCheck) {
        var correctFormat = correctStateFormat(stateToCheck);
        if (correctFormat.length() == 2)
            validateState(correctFormat, State::abbreviation);
        else if (correctFormat.length() > 2)
            validateState(correctFormat, State::fullName);
        return stateToCheck;
    }

    private static void validateState(String state, Function<State, String> getStateProperty) {
        String stateTrimmed = state.trim().toUpperCase();
        if (!getStateProperty.apply(State.getState(state)).equals(stateTrimmed)) {
            throw new IllegalArgumentException(new StateValidationException(state, NONEXISTING));
        }
    }

}
