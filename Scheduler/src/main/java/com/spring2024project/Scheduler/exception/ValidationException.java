package com.spring2024project.Scheduler.exception;


import java.util.Objects;

/**
 * This class represents a skeletal implementation of various custom exceptions. The purpose for this is to aid implementation
 * of other custom exceptions to enhance failure-capture information when developing this application. It is expected that
 * future custom exceptions extend this class, and the developer adds necessary values to Cause that enhances the specificity
 * of the exception.
 * @Author Shankar Choudhury
 */
public abstract class ValidationException extends Exception {
    public enum Cause {
        NULL_STRING("String cannot be null"),
        EMPTY_STRING("String cannot be empty"),
        BLANK_STRING("String cannot be blank"),
        FORMAT("String has incorrect format"),
        NULL_LIST("List cannot be null"),
        EMPTY_LIST("List cannot be empty"),
        NULL_ELEMENTS("This element is null: "),
        INVALID_ELEMENTS("This element is invalid: "),
        NONEXISTING("Element does not exist");

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

    protected final Cause cause;
    protected final String explanation;

    protected ValidationException(Cause cause) {
        assert Objects.nonNull(cause);
        this.cause = cause;
        explanation = cause.name() + ": " + cause.description;
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
}
