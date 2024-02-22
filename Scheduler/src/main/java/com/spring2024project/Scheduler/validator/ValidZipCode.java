package com.spring2024project.Scheduler.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidZipCode {
    String message() default "Zip code does not exist in the database";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
