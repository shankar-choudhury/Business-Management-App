package com.spring2024project.Scheduler.validatingMethods;

import java.util.*;
import java.util.function.Predicate;

/**
 * This class provides utility methods for general validation tasks.
 */
public final class GeneralValidator {

    private GeneralValidator(){}

    /**
     * Verifies that all inputs are not null.
     * @param args The arguments to check for nullity.
     * @throws NullPointerException if any argument is null.
     */
    public static void verifyNonNull(Object... args) {
        Arrays.stream(args).forEach(Objects::requireNonNull);
    }

    public static <T> Set<T> getOrEmpty(Set<T> toCheck) {
        return Objects.requireNonNullElseGet(toCheck, HashSet::new);
    }

    /**
     * Validates an integer value against a given condition.
     * @param integer The integer value to validate.
     * @param validCondition The condition predicate to test the integer value against.
     * @return The validated integer value if it satisfies the condition.
     * @throws IllegalArgumentException if the integer value does not satisfy the condition.
     */
    static Integer validInt(int integer, Predicate<Integer> validCondition) {
        return validCondition.test(integer) ? integer : throwException(integer);
    }

    /**
     * Throws an IllegalArgumentException with a message indicating a bad value.
     * @param val The value that caused the exception.
     * @return An IllegalArgumentException with a descriptive message.
     */
    private static Integer throwException(int val) {
        throw new IllegalArgumentException("Bad value: " + val);
    }

}