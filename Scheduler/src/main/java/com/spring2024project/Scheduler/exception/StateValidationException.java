package com.spring2024project.Scheduler.exception;

import lombok.Getter;

@Getter
public class StateValidationException extends ValidationException {
    private final String nonExistingState;
    protected StateValidationException(String nonExistingState, Cause cause) {
        super(cause);
        this.nonExistingState = nonExistingState;
    }

}
