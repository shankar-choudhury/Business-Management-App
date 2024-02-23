package com.spring2024project.Scheduler.validatingMethods;

import com.spring2024project.Scheduler.constantValues.State;
import com.spring2024project.Scheduler.exception.StringValidationException;
import static com.spring2024project.Scheduler.exception.ValidationException.Cause.*;

import com.spring2024project.Scheduler.exception.ValidationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringValidator {

    private static String validateString(String toCheck, Predicate<String> invalidCondition, ValidationException.Cause cause) {
        if (invalidCondition.test(toCheck))
            throw new IllegalArgumentException(new StringValidationException(toCheck, cause));
        return toCheck;
    }

    /**
     * Performs basic checks on the given string, including null, empty, and blank checks. This method is designed to be
     * used on any String and should be called prior to any other validator methods
     * @param toCheck The string to check.
     * @return The input string if it passes all checks.
     * @throws IllegalArgumentException if any check fails.
     */
    public static String verifyNonNullEmptyOrBlank(String toCheck) {
        validateString(toCheck, Objects::isNull, NULL_STRING);
        validateString(toCheck, String::isEmpty, EMPTY_STRING);
        validateString(toCheck, String::isBlank, BLANK_STRING);
        return toCheck;
    }

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

    public static String correctCCNumberFormat(String creditCardNumber) {
        return validateString(creditCardNumber,
                string -> string.matches("^(?:4[0-9]{12}(?:[0-9]{3})?|[25][1-7][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})$"),
                FORMAT);
    }

    public static String correctNameFormat(String name) {
        return validateString(name, string -> string.matches("\\p{Alpha}+"), FORMAT);
    }

    public static String correctBuildingNumFormat(String buildingNumber) {
        return validateString(buildingNumber, string -> string.matches("^\\d+\\w*$"), FORMAT);
    }

    public static String correctStreetFormat(String street) {
        return validateString(street,
                string -> string.matches("\\d*[ ](?:[A-Za-z0-9.-]+[ ]?)+(?:Avenue|Lane|Road|Boulevard|Drive|Street|Ave|Dr|Rd|Blvd|Ln|St)\\.?"),
                FORMAT);
    }

    public static String correctCityFormat(String city) {
        return validateString(city,
                string -> string.matches("^[a-zA-Z\\u0080-\\u024F]+(?:. |-| |')*([1-9a-zA-Z\\u0080-\\u024F]+(?:. |-| |'))*[a-zA-Z\\u0080-\\u024F]*$"),
                FORMAT);
    }

    public static String correctState(String state) {
        return validateString(
                correctStateFormat(state),
                stateName -> State.find(stateName).name().equals(stateName), NONEXISTING);
    }

    public static String correctStateFormat(String state) {
        return validateString(state, string -> string.matches("^[A-Za-z\\s]+$"), FORMAT);
    }
}
