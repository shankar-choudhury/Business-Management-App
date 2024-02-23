package com.spring2024project.Scheduler.exception;


import lombok.Getter;

import java.util.Objects;

/**
 * This class represents a set of utility methods for checking String values for various bad inputs.
 * It provides methods for basic string checks such as null, empty, and blank, as well as email format validation.
 * @Author Shankar Choudhury
 */
@Getter
public class StringValidationException extends ValidationException {
    private final String badString;

    public StringValidationException(String badString, Cause cause) {
        super(Objects.requireNonNull(cause));
        this.badString = badString;
    }

    /**
     * Gets the bad string that caused the exception.
     *
     * @return The bad string.
     */
    public String badString() {
        return badString;
    }
}