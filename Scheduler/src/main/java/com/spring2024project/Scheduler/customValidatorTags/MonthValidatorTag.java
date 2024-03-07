package com.spring2024project.Scheduler.customValidatorTags;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * A custome validator class to validate a Month field during bean creation and persistence
 * @Author Shankar Choudhury
 */
public class MonthValidatorTag implements ConstraintValidator<ValidMonth, Integer> {
    @Override
    public boolean isValid(Integer month, ConstraintValidatorContext context) {
        return month >= 1 && month <= 12;
    }
}
