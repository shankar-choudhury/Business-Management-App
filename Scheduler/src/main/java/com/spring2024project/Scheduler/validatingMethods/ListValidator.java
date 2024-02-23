package com.spring2024project.Scheduler.validatingMethods;

import com.spring2024project.Scheduler.exception.ListValidationException;
import com.spring2024project.Scheduler.exception.ValidationException;

import static com.spring2024project.Scheduler.exception.ValidationException.Cause.*;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ListValidator {
    public static <T> List<T> verifyNoElementsMatch(List<T> toCheck, Predicate<T> invalidCondition) {
        List<T> invalidElementsList = toCheck.stream()
                .filter(invalidCondition)
                .collect(Collectors.toList());

        return invalidElementsList.isEmpty() ? toCheck : throwException(toCheck, invalidElementsList, INVALID_ELEMENTS);
    }

    public static <T> List<T> verifyNonNull(List<T> toCheck) {
        return verify(toCheck, Objects::nonNull, NULL_LIST);
    }

    public static <T> List<T> verifyNonEmpty(List<T> toCheck) {
        return verify(toCheck, list -> !list.isEmpty(), EMPTY_LIST);
    }

    public static <T> List<T> verifyNonNullElements(List<T> toCheck) {
        return verify(toCheck, list -> list.stream().noneMatch(Objects::isNull), NULL_ELEMENTS);
    }

    private static <T> List<T> throwException(List<T> badElementsList, ValidationException.Cause cause) {
        throw new IllegalArgumentException(new ListValidationException(badElementsList, cause));
    }

    private static <T> List<T> throwException(List<T> originalList, List<T> badElementsList, ValidationException.Cause cause) {
        throw new IllegalArgumentException(new ListValidationException(originalList, badElementsList, cause));
    }

    private static <T> List<T> verify(List<T> toCheck, Predicate<List<T>> validCondition, ValidationException.Cause cause) {
        return validCondition.test(toCheck) ? toCheck : throwException(toCheck, cause);
    }

}
