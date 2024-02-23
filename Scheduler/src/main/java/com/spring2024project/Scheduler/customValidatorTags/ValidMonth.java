package com.spring2024project.Scheduler.customValidatorTags;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMonth {
    String message() default "Month is not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
