package com.spring2024project.Scheduler.validatingMethods;

import com.spring2024project.Scheduler.constantValues.State;
import com.spring2024project.Scheduler.exception.StateValidationException;
import com.spring2024project.Scheduler.exception.StringValidationException;
import static com.spring2024project.Scheduler.exception.ValidationException.Cause.*;

import com.spring2024project.Scheduler.exception.ValidationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * This class provides methods for validating strings, including checks for null, empty, and blank strings,
 * as well as specific formats such as email addresses, phone numbers, credit card numbers, names, addresses, cities, and states.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringValidator {

    /**
     * Validates a string against a given condition and throws an exception if the condition is not met.
     * @param toCheck The string to check.
     * @param validCondition The condition to check against.
     * @param cause The cause of the validation exception.
     * @return The input string if it passes the validation.
     * @throws IllegalArgumentException if the validation condition is not met.
     */
    private static String validateString(String toCheck, Predicate<String> validCondition, ValidationException.Cause cause) {
        if (!validCondition.test(toCheck))
            throw new IllegalArgumentException(new StringValidationException(toCheck, cause));
        return toCheck;
    }

    /**
     * Validates the format of the given state abbreviation.
     * @param stateToCheck The state abbreviation to validate.
     * @return The input state abbreviation if it is valid.
     * @throws IllegalArgumentException if the state abbreviation is not valid.
     */
    private static String validateState(String stateToCheck) {
        if (!State.find(stateToCheck).abbreviation().equals(stateToCheck.trim().toUpperCase())
        || !State.find(stateToCheck).fullName().equals(stateToCheck.trim().toUpperCase()))
            throw new IllegalArgumentException(new StateValidationException(stateToCheck, NONEXISTING));
        return stateToCheck;
    }

    /**
     * Performs basic checks on the given string, including null, empty, and blank checks. This method is designed to be
     * used on any String and should be called prior to any other validator methods
     * @param toCheck The string to check.
     * @return The input string if it passes all checks.
     * @throws IllegalArgumentException if any check fails.
     */
    public static String verifyNonNullEmptyOrBlank(String toCheck) {
        validateString(toCheck, Objects::nonNull, NULL_STRING);
        validateString(toCheck, string -> !string.isEmpty(), EMPTY_STRING);
        validateString(toCheck, string -> !string.isBlank(), BLANK_STRING);
        return toCheck;
    }

    /**
     * Performs basic checks on the given strings, including null, empty, and blank checks. This method is designed to be
     * used on multiple strings and should be called prior to any other validator methods
     * @param toCheck The strings to check.
     * @throws IllegalArgumentException if any check fails.
     */
    public static void verifyNonNullEmptyOrBlank(String... toCheck) {
        Arrays.stream(toCheck).forEach(StringValidator::verifyNonNullEmptyOrBlank);
    }

    /**
     * Validates the format of the given email address.
     * @param email The email address to validate.
     * @return The input email address if it is in a valid format.
     * @throws IllegalArgumentException if the email format is incorrect.
     */
    public static String correctEmailFormat(String email) {
        return validateString(verifyNonNullEmptyOrBlank(email),
                e -> e.matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$"),
                FORMAT);
    }

    /**
     * Validates the format of the given phone number.
     * @param phoneNumber The phone number to validate.
     * @return The input phone number if it is in a valid format.
     * @throws IllegalArgumentException if the phone number format is incorrect.
     */
    public static String correctPhoneNumberFormat(String phoneNumber) {
        return validateString(phoneNumber, p -> p.matches("^(\\(?\\d{3}\\)?[-.\\s]?)?\\d{3}[-.\\s]?\\d{4}$"), FORMAT);
    }

    /**
     * Validates the format of the given credit card number.
     * @param creditCardNumber The credit card number to validate.
     * @return The input credit card number if it is in a valid format.
     * @throws IllegalArgumentException if the credit card number format is incorrect.
     */
    public static String correctCCNumberFormat(String creditCardNumber) {
        return validateString(creditCardNumber,
                string -> string.matches("^(?:4[0-9]{12}(?:[0-9]{3})?|[25][1-7][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})$"),
                FORMAT);
    }

    /**
     * Validates the format of the given name.
     * @param name The name to validate.
     * @return The input name if it is in a valid format.
     * @throws IllegalArgumentException if the name format is incorrect.
     */
    public static String correctNameFormat(String name) {
        return validateString(name, string -> string.matches("\\p{Alpha}+"), FORMAT);
    }

    /**
     * Validates the format of the given building number.
     * @param buildingNumber The building number to validate.
     * @return The input building number if it is in a valid format.
     * @throws IllegalArgumentException if the building number format is incorrect.
     */
    public static String correctBuildingNumFormat(String buildingNumber) {
        return validateString(buildingNumber, string -> string.matches("^\\d+\\w*$"), FORMAT);
    }

    /**
     * Validates the format of the given street name.
     * @param street The street name to validate.
     * @return The input street name if it is in a valid format.
     * @throws IllegalArgumentException if the street name format is incorrect.
     */
    public static String correctStreetFormat(String street) {
        return validateString(street,
                string -> string.matches("\\d*[ ](?:[A-Za-z0-9.-]+[ ]?)+(?:Avenue|Lane|Road|Boulevard|Drive|Street|Ave|Dr|Rd|Blvd|Ln|St)\\.?"),
                FORMAT);
    }

    /**
     * Validates the format of the given city name.
     * @param city The city name to validate.
     * @return The input city name if it is in a valid format.
     * @throws IllegalArgumentException if the city name format is incorrect.
     */
    public static String correctCityFormat(String city) {
        return validateString(city,
                string -> string.matches("^[a-zA-Z\\u0080-\\u024F]+(?:. |-| |')*([1-9a-zA-Z\\u0080-\\u024F]+(?:. |-| |'))*[a-zA-Z\\u0080-\\u024F]*$"),
                FORMAT);
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
     * Validates the format of the given state name.
     * @param state The state name to validate.
     * @return The input state name if it is in a valid format.
     * @throws IllegalArgumentException if the state name format is incorrect.
     */
    public static String correctStateFormat(String state) {
        return validateString(state, string -> string.matches("^[A-Za-z\\s]+$"), FORMAT);
    }
}