package com.spring2024project.Scheduler.validatingMethods;

import com.spring2024project.Scheduler.exception.ListValidationException;
import com.spring2024project.Scheduler.exception.ValidationException;

import static com.spring2024project.Scheduler.exception.ValidationException.Cause.*;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This class provides methods for validating lists, including checks for null lists, empty lists,
 * and lists with null elements.
 */
public class ListValidator {

    /**
     * Verifies that no elements in the list match the given invalid condition.
     * @param toCheck The list to check.
     * @param validCondition The condition to check against. The condition is specified for what the element should match
     * @return The original list if no elements match the condition.
     * @throws IllegalArgumentException with a ListValidationException containing details of the failed validation if
     * any of the elements match the condition.
     */
    public static <T> List<T> verifyNoElementsMatch(List<T> toCheck, Predicate<T> validCondition) {
        return verify(toCheck, list -> toCheck.stream().allMatch(validCondition), INVALID_ELEMENTS);
    }

    /**
     * Verifies that the given list is not null.
     * @param toCheck The list to check.
     * @param <T> The type of elements in the list.
     * @return The input list if it is not null.
     * @throws IllegalArgumentException if the list is null.
     */
    public static <T> List<T> verifyNonNull(List<T> toCheck) {
        return verify(toCheck, Objects::nonNull, NULL_LIST);
    }

    /**
     * Verifies that the given list is not empty.
     * @param toCheck The list to check.
     * @param <T> The type of elements in the list.
     * @return The input list if it is not empty.
     * @throws IllegalArgumentException if the list is empty.
     */
    public static <T> List<T> verifyNonEmpty(List<T> toCheck) {
        return verify(toCheck, list -> !list.isEmpty(), EMPTY_LIST);
    }

    /**
     * Verifies that the given list does not contain any null elements.
     * @param toCheck The list to check.
     * @param <T> The type of elements in the list.
     * @return The input list if it does not contain null elements.
     * @throws IllegalArgumentException if the list contains null elements.
     */
    public static <T> List<T> verifyNonNullElements(List<T> toCheck) {
        return verify(toCheck, list -> list.stream().noneMatch(Objects::isNull), NULL_ELEMENTS);
    }

    /**
     * Throws a list validation exception for the given list determined to be a bad list.
     * @param badElementsList The checked list deemed necessary to throw an exception for.
     * @param cause The cause of the validation error.
     * @param <T> The type of elements in the list.
     * @return Never returns, always throws an exception.
     * @throws IllegalArgumentException Always thrown with a ListValidationException containing details of the failed validation.
     */
    private static <T> List<T> throwException(List<T> badElementsList, ValidationException.Cause cause) {
        throw new IllegalArgumentException(new ListValidationException(badElementsList, cause));
    }

    /**
     * Throws a list validation exception for the given bad elements list and cause.
     * @param originalList The original list being validated.
     * @param badElementsList The list of elements that failed validation.
     * @param cause The cause of the validation failure.
     * @param <T> The type of elements in the list.
     * @return Never returns, always throws an exception.
     * @throws IllegalArgumentException Always thrown with a ListValidationException containing details of the failed validation.
     */
    private static <T> List<T> throwException(List<T> originalList, List<T> badElementsList, ValidationException.Cause cause) {
        throw new IllegalArgumentException(new ListValidationException(originalList, badElementsList, cause));
    }

    /**
     * Verifies that the given list matches the valid condition
     * @param toCheck The list to check
     * @param validCondition The condition to check against
     * @param cause The cause of the validation failure
     * @param <T> The type of elements in the list.
     * @return The list to check if it passes the valid condition
     * @throws IllegalArgumentException
     */
    private static <T> List<T> verify(List<T> toCheck, Predicate<List<T>> validCondition, ValidationException.Cause cause) {
        return validCondition.test(toCheck) ? toCheck : throwException(toCheck, cause);
    }

}