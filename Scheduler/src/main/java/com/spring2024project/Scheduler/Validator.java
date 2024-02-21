package com.spring2024project.Scheduler;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

public final class Validator {

    private Validator(){}

    public static void verifyNonNull(Object... args) {
        Arrays.stream(args).forEach(Objects::requireNonNull);
    }
}
