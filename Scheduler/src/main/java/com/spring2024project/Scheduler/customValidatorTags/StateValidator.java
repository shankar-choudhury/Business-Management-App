package com.spring2024project.Scheduler.customValidatorTags;

import com.spring2024project.Scheduler.constantValues.State;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StateValidator implements ConstraintValidator<ValidState, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return State.find(value.trim().toUpperCase()) != State.EMPTY;
    }
}
