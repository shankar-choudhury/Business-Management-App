package com.spring2024project.Scheduler.validator;

import com.spring2024project.Scheduler.exception.StringValidationException;

import java.util.Arrays;
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

    /**
     * Performs basic checks on the given string, including null, empty, and blank checks. This method is designed to be
     * used on any String and should be called prior to any other validator methods
     * @param toCheck The string to check.
     * @return The input string if it passes all checks.
     * @throws IllegalArgumentException if any check fails.
     */
    public static String verifyNonNullEmptyOrBlank(String toCheck) {
        validateString(toCheck, Objects::nonNull, StringValidationException.Cause.NULL);
        validateString(toCheck, String::isEmpty, StringValidationException.Cause.EMPTY);
        validateString(toCheck, String::isBlank, StringValidationException.Cause.BLANK);
        return toCheck;
    }

    /**
     * Validates the format of the given email address.
     * @param email The email address to validate.
     * @return The input email address if it is in a valid format.
     * @throws IllegalArgumentException if the email format is incorrect.
     */
    public static String emailCheck(String email) {
        validateString(verifyNonNullEmptyOrBlank(email), e -> e.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"), StringValidationException.Cause.FORMAT);
        return email;
    }

    /**
     * Validates the format of the given phone number.
     * @param phoneNumber The phone number to validate.
     * @return The input phone number if it is in a valid format.
     * @throws IllegalArgumentException if the phone number format is incorrect.
     */
    public static String phoneNumberCheck(String phoneNumber) {
        validateString(verifyNonNullEmptyOrBlank(phoneNumber), p -> p.length() == 10, StringValidationException.Cause.FORMAT);
        validateString(verifyNonNullEmptyOrBlank(phoneNumber), p -> p.matches("[0-9]+"), StringValidationException.Cause.FORMAT);
        return phoneNumber;
    }

    public static void validateString(String toCheck, Predicate<String> p, StringValidationException.Cause cause) {
        if (!p.test(toCheck))
            throw new IllegalArgumentException(new StringValidationException(toCheck, cause));
    }
}
