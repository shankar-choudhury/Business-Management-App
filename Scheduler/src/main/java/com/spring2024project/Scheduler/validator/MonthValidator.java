package com.spring2024project.Scheduler.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class MonthValidator implements ConstraintValidator<ValidMonth, Integer> {
    @Override
    public boolean isValid(Integer month, ConstraintValidatorContext context) {
        return month >= 1 && month <= 12;
    }
}
