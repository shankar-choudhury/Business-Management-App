package com.spring2024project.Scheduler.exception;

import lombok.Getter;

/**
 * This class represents an exception when there is an attempt to pass an invalid state is made when creating an address.
 * Note that the main cause is NONEXISTING, since the application is built to check the validity of the state by ensuring
 * it exists. Other exceptions will be due to the format of the name of the state of which an attempt was made to pass into
 * an address, so a more appropriate Exception would be the StringValidationException.
 * @Author Shankar Choudhury
 */
@Getter
public class StateValidationException extends ValidationException {
    private final String nonExistingState;
    public StateValidationException(String nonExistingState, Cause cause) {
        super(cause);
        this.nonExistingState = nonExistingState;
    }

}
