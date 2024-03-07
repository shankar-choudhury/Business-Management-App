package com.spring2024project.Scheduler.validatingMethods;

import com.spring2024project.Scheduler.exception.StringValidationException;
import static com.spring2024project.Scheduler.exception.ValidationException.Cause.*;

import com.spring2024project.Scheduler.exception.ValidationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * This class provides methods for validating strings, including checks for null, empty, and blank strings,
 * as well as specific formats such as email addresses, phone numbers, credit card numbers, names, addresses, cities, and states.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringValidator {


    static String validateString(String toCheck, Predicate<String> validCondition, ValidationException.Cause cause) {
        if (!validCondition.test(toCheck))
            throw new IllegalArgumentException(new StringValidationException(toCheck, cause));
        return toCheck;
    }

    public static String verifyNonNullEmptyOrBlank(String toCheck) {
        validateString(toCheck, Objects::nonNull, NULL_STRING);
        validateString(toCheck, string -> !string.isEmpty(), EMPTY_STRING);
        validateString(toCheck, string -> !string.isBlank(), BLANK_STRING);
        return toCheck;
    }

    public static void verifyNonNullEmptyOrBlank(String... toCheck) {
        Arrays.stream(toCheck).forEach(StringValidator::verifyNonNullEmptyOrBlank);
    }

}