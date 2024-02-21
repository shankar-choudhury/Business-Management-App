package com.spring2024project.Scheduler.exception;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * This class represents a set of utility methods for checking String values for various bad inputs.
 * It provides methods for basic string checks such as null, empty, and blank, as well as email format validation.
 * @Author Shankar Choudhury
 */
public class BadStringException extends Exception {
    /**
     * Enum representing the possible causes of bad string inputs.
     */
    public enum Cause {
        NULL("String cannot be null"),
        EMPTY("String cannot be blank"),
        BLANK("String cannot be blank"),
        FORMAT("String has incorrect format");

        private final String description;

        Cause(String description) {
            this.description = description;
        }

        /**
         * Gets the description of the cause.
         * @param cause The cause enum value.
         * @return A string describing the cause.
         */
        public static String getDescription(Cause cause) {
            return cause.name() + ": " + cause.description;
        }
    }

    private final String badString;
    private final String explanation;

    /**
     * Constructs a BadStringException with the given bad string and cause.
     * @param badString The bad string that caused the exception.
     * @param cause The cause of the exception.
     */
    public BadStringException(String badString, Cause cause) {
        this.badString = badString;
        this.explanation = Cause.getDescription(cause);
    }

    /**
     * Gets the explanation of the exception.
     * @return A string describing the cause of the exception.
     */
    public String explanation() {
        return explanation;
    }

    /**
     * Gets the bad string that caused the exception.
     * @return The bad string.
     */
    public String badString() {
        return badString;
    }

    /**
     * Performs basic checks on the given string, including null, empty, and blank checks.
     * @param toCheck The string to check.
     * @return The input string if it passes all checks.
     * @throws IllegalArgumentException if any check fails.
     */
    public static String basicCheck(String toCheck) {
        validate(toCheck, Objects::nonNull, Cause.NULL);
        validate(toCheck, String::isEmpty, Cause.EMPTY);
        validate(toCheck, String::isBlank, Cause.BLANK);
        return toCheck;
    }

    /**
     * Validates the format of the given email address.
     * @param email The email address to validate.
     * @return The input email address if it is in a valid format.
     * @throws IllegalArgumentException if the email format is incorrect.
     */
    public static String emailFormCheck(String email) {
        validate(email, e -> e.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"), Cause.FORMAT);
        return email;
    }

    private static void validate(String toCheck, Predicate<String> p, Cause cause) {
        if (!p.test(toCheck))
            throw new IllegalArgumentException(new BadStringException(toCheck, cause));
    }
}