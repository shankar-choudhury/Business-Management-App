package com.spring2024project.Scheduler.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidState {
    String message() default "Invalid state name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}