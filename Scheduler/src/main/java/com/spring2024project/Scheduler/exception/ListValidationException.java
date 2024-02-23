package com.spring2024project.Scheduler.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ListValidationException extends ValidationException {
    private List<?> invalidList;
    private List<?> invalidElements = List.of();

    public ListValidationException(List<?> invalidList, Cause cause) {
        super(cause);
        this.invalidList = invalidList;
    }

    public ListValidationException(List<?> invalidList, List<?> invalidElements, Cause cause) {
        super(cause);
        this.invalidList = invalidList;
        this.invalidElements = invalidElements;
    }
}
