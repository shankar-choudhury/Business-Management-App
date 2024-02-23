package com.spring2024project.Scheduler.customValidatorTags;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MonthValidator implements ConstraintValidator<ValidMonth, Integer> {
    @Override
    public boolean isValid(Integer month, ConstraintValidatorContext context) {
        return month >= 1 && month <= 12;
    }
}
