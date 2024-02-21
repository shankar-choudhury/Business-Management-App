package com.spring2024project.Scheduler;

import com.spring2024project.Scheduler.exception.BadStringException;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

public final class Validator {

    private Validator(){}

    /**
     * Verify that all inputs are not null
     * @param args Arguments to check for nullity
     * @throws NullPointerException If any argument is null
     */
    public static void verifyNonNull(Object... args) {
        Arrays.stream(args).forEach(Objects::requireNonNull);
    }

    /**
     * Performs basic checks on the given string, including null, empty, and blank checks.
     * @param toCheck The string to check.
     * @return The input string if it passes all checks.
     * @throws IllegalArgumentException if any check fails.
     */
    public static String basicCheck(String toCheck) {
        validate(toCheck, Objects::nonNull, BadStringException.Cause.NULL);
        validate(toCheck, String::isEmpty, BadStringException.Cause.EMPTY);
        validate(toCheck, String::isBlank, BadStringException.Cause.BLANK);
        return toCheck;
    }

    /**
     * Validates the format of the given email address.
     * @param email The email address to validate.
     * @return The input email address if it is in a valid format.
     * @throws IllegalArgumentException if the email format is incorrect.
     */
    public static String emailCheck(String email) {
        validate(email, e -> e.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"), BadStringException.Cause.FORMAT);
        return email;
    }

    /**
     * Validates the format of the given phone number.
     * @param phoneNumber The phone number to validate.
     * @return The input phone number if it is in a valid format.
     * @throws IllegalArgumentException if the phone number format is incorrect.
     */
    public static String phoneNumberCheck(String phoneNumber) {
        validate(phoneNumber, p -> p.length() == 10, BadStringException.Cause.FORMAT);
        validate(phoneNumber, p -> p.matches("[0-9]+"), BadStringException.Cause.FORMAT);
        return phoneNumber;
    }

    private static void validate(String toCheck, Predicate<String> p, BadStringException.Cause cause) {
        if (!p.test(toCheck))
            throw new IllegalArgumentException(new BadStringException(toCheck, cause));
    }
}
