package com.spring2024project.Scheduler.exception;


/**
 * This class represents a set of utility methods for checking String values for various bad inputs.
 * It provides methods for basic string checks such as null, empty, and blank, as well as email format validation.
 * @Author Shankar Choudhury
 */
public class StringValidationException extends Exception {
    /**
     * Enum representing the possible causes of bad string inputs.
     */
    public enum Cause {
        NULL("String cannot be null"),
        EMPTY("String cannot be empty"),
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
    private final Cause cause;
    private final String explanation;

    /**
     * Constructs a BadStringException with the given bad string and cause.
     * @param badString The bad string that caused the exception.
     * @param cause The cause of the exception.
     */
    public StringValidationException(String badString, Cause cause) {
        this.badString = badString;
        this.cause = cause;
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
     * Return the cause of this exception
     * @return The cause of this exception
     */
    public Cause cause() {
        return cause;
    }

    /**
     * Gets the bad string that caused the exception.
     * @return The bad string.
     */
    public String badString() {
        return badString;
    }
}