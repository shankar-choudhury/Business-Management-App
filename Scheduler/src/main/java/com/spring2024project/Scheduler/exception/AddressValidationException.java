package com.spring2024project.Scheduler.exception;

import com.spring2024project.Scheduler.entity.Address;
import lombok.Getter;

@Getter
public class AddressValidationException extends ValidationException{
    private final Address invalidAddress;

    public AddressValidationException(Address invalidAddress, Cause cause) {
        super(cause);
        this.invalidAddress = invalidAddress;
    }
}
