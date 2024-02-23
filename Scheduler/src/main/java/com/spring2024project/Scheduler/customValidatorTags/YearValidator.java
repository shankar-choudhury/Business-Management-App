package com.spring2024project.Scheduler.customValidatorTags;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Year;

public class YearValidator implements ConstraintValidator<ValidYearRange, Integer> {
    @Override
    public boolean isValid(Integer year, ConstraintValidatorContext context) {
        if (year == null) {
            return true; // Let other validators handle null values
        }

        int currentYear = Year.now().getValue();
        int futureYear = currentYear + 5;

        return year >= currentYear && year <= futureYear;
    }
}

