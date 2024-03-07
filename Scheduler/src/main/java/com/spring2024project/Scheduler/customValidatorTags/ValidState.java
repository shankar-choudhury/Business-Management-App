package com.spring2024project.Scheduler.customValidatorTags;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StateValidatorTag.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidState {
    String message() default "Invalid state name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}