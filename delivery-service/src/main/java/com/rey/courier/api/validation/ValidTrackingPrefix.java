package com.rey.courier.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TrackingPrefixValidator.class)
@Documented
public @interface ValidTrackingPrefix { // THIS MUST MATCH FILENAME
    String message() default "Invalid tracking prefix";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}