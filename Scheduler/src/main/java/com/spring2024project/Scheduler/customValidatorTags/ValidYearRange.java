package com.spring2024project.Scheduler.customValidatorTags;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = YearValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidYearRange {
    String message() default "Year is outside of valid range";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
