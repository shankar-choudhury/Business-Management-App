package com.spring2024project.Scheduler.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
public class ListValidationException extends Exception {
    public enum Cause {
        NULL("List cannot be null"),
        EMPTY("List cannot be empty"),
        NULL_ELEMENT("This element is null: "),
        INVALID_ELEMENT("This element is invalid: ");

        private final String description;

        Cause(String description) {this.description = description;}

        /**
         * Gets the description of the cause.
         * @param cause The cause enum value.
         * @return A string describing the cause.
         */
        public static String getDescription(Cause cause) {
            return cause.name() + ": " + cause.description;
        }
    }

    private List<?> badList = List.of();
    private final Cause cause;
    private final String explanation;
    private int index = -1;

    public ListValidationException(Cause cause) {
        this.cause = cause;
        explanation = Cause.getDescription(cause);
    }

    public ListValidationException(List<?> badList, Cause cause) {
        this.badList = badList;
        this.cause = cause;
        explanation = Cause.getDescription(cause);
    }

    public ListValidationException(List<?> badList, int indexOfInvalidElement, Cause cause) {
        this.badList = badList;
        this.cause = cause;
        this.index = indexOfInvalidElement;
        explanation = Cause.getDescription(cause);
    }
}
