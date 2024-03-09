package com.spring2024project.Scheduler.customValidatorTags;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Year;

/**
 * A custom validation class to validate a given year. Primarily used for validating bean with annotation tags
 * TODO: implement java.time when creating scheduler interface
 * @Author Shankar Choudhury
 */
public class YearValidatorTag implements ConstraintValidator<ValidYearRange, Integer> {
    @Override
    public boolean isValid(Integer year, ConstraintValidatorContext context) {
        if (year == null) {
            return true;
        }

        int currentYear = Year.now().getValue();
        int futureYear = currentYear + 5;

        return year >= currentYear && year <= futureYear;
    }
}

