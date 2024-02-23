package com.spring2024project.Scheduler.validator;

import com.spring2024project.Scheduler.exception.ListValidationException;
import com.spring2024project.Scheduler.exception.StringValidationException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public final class ValidatingMethods {

    private ValidatingMethods(){}

    /**
     * Verify that all inputs are not null
     * @param args Arguments to check for nullity
     * @throws NullPointerException If any argument is null
     */
    public static void verifyNonNull(Object... args) {
        Arrays.stream(args).forEach(Objects::requireNonNull);
    }

    private static String validateString(String toCheck, Predicate<String> p, StringValidationException.Cause cause) {
        if (p.test(toCheck))
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
        validateString(toCheck, Objects::isNull, StringValidationException.Cause.NULL);
        validateString(toCheck, String::isEmpty, StringValidationException.Cause.EMPTY);
        validateString(toCheck, String::isBlank, StringValidationException.Cause.BLANK);
        return toCheck;
    }

    public static void verifyNonNullEmptyOrBlank(String... toCheck) {
        Arrays.stream(toCheck).forEach(ValidatingMethods::verifyNonNullEmptyOrBlank);
    }

    /**
     * Validates the format of the given email address.
     * @param email The email address to validate.
     * @return The input email address if it is in a valid format.
     * @throws IllegalArgumentException if the email format is incorrect.
     */
    public static String correctEmailFormat(String email) {
        validateString(verifyNonNullEmptyOrBlank(email),
                e -> e.matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$"),
                StringValidationException.Cause.FORMAT);
        return email;
    }

    /**
     * Validates the format of the given phone number.
     * @param phoneNumber The phone number to validate.
     * @return The input phone number if it is in a valid format.
     * @throws IllegalArgumentException if the phone number format is incorrect.
     */
    public static String correctPhoneNumberFormat(String phoneNumber) {
        validateString(verifyNonNullEmptyOrBlank(phoneNumber), p -> p.length() == 10, StringValidationException.Cause.FORMAT);
        validateString(verifyNonNullEmptyOrBlank(phoneNumber), p -> p.matches("^(\\(?\\d{3}\\)?[-.\\s]?)?\\d{3}[-.\\s]?\\d{4}$"), StringValidationException.Cause.FORMAT);
        return phoneNumber;
    }

    public static String correctNameFormat(String name) {
        return validateString(name, string -> string.matches("\\p{Alpha}+"), StringValidationException.Cause.FORMAT);
    }

    public static String correctStateFormat(String state) {
        return validateString(state, string -> string.matches("^[A-Za-z\\s]+$"), StringValidationException.Cause.FORMAT);
    }

    public static <T> List<T> verifyNoElementsMatch(List<T> toCheck, Predicate<T> invalidCondition) {
        toCheck.stream()
                .filter(invalidCondition)
                .findFirst()
                .map(element -> {
                    throw new IllegalArgumentException(
                            new ListValidationException(toCheck, toCheck.indexOf(element),
                                    ListValidationException.Cause.INVALID_ELEMENT));
                });
        return toCheck;
    }

    public static <T> List<T> verifyList(List<T> toCheck) {
        if (Objects.isNull(toCheck))
            throw new IllegalArgumentException(
                    new ListValidationException(ListValidationException.Cause.NULL));
        return toCheck;
    }

    public static <T> List<T> verifyNonEmpty(List<T> toCheck) {
        if (toCheck.isEmpty())
            throw new IllegalArgumentException(
                    new ListValidationException(toCheck, ListValidationException.Cause.EMPTY));
        return toCheck;
    }
}
