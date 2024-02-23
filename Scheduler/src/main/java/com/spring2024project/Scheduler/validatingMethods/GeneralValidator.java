package com.spring2024project.Scheduler.validatingMethods;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;
import java.time.Year;

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

    /**
     * Verifies that the given month is valid (between 1 and 12).
     * @param month The month to validate.
     * @return The input month if it is valid.
     * @throws IllegalArgumentException if the month is not valid.
     */
    public static Integer verifyMonth(int month) {
        return validInt(month, val -> val > 0 && val < 13);
    }

    /**
     * Verifies that the given year is within a specified range.
     * @param year The year to validate.
     * @param additionalYears The additional years to add to the current year for the upper bound of the range.
     * @return The input year if it is within the specified range.
     * @throws IllegalArgumentException if the year is not within the specified range.
     */
    public static Integer verifyYearInRange(int year, int additionalYears) {
        int currentYear = Year.now().getValue();
        return validInt(year, val -> val >= currentYear && val <= currentYear + additionalYears);
    }

    /**
     * Validates an integer value against a given condition.
     * @param integer The integer value to validate.
     * @param validCondition The condition predicate to test the integer value against.
     * @return The validated integer value if it satisfies the condition.
     * @throws IllegalArgumentException if the integer value does not satisfy the condition.
     */
    private static Integer validInt(int integer, Predicate<Integer> validCondition) {
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