package com.spring2024project.Scheduler.validatingMethods;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;
import java.time.Year;

public final class GeneralValidator {

    private GeneralValidator(){}

    /**
     * Verify that all inputs are not null
     * @param args Arguments to check for nullity
     * @throws NullPointerException If any argument is null
     */
    public static void verifyNonNull(Object... args) {
        Arrays.stream(args).forEach(Objects::requireNonNull);
    }

    public static Integer verifyMonth(int month) {
        return validInt(month, val -> val > 0 && val < 13);
    }

    public static Integer verifyYearInRange(int year, int additionalYears) {
        int currentYear = Year.now().getValue();
        return validInt(year, val -> val >= currentYear && val <= currentYear + additionalYears);
    }

    private static Integer validInt(int integer, Predicate<Integer> validCondition) {
        return validCondition.test(integer) ? integer : throwException(integer);
    }

    private static Integer throwException(int val) {
        throw new IllegalArgumentException("Bad value: " + val);
    }

}
