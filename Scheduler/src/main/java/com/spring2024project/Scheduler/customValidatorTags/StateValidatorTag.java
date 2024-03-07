package com.spring2024project.Scheduler.customValidatorTags;

import com.spring2024project.Scheduler.constantValues.State;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * A custom validator class to validate bean creation. Used to check that bean's US State is an existing state.
 * @Author Shankar Choudhury
 */
public class StateValidatorTag implements ConstraintValidator<ValidState, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return State.find(value.trim().toUpperCase()) != State.EMPTY;
    }
}
