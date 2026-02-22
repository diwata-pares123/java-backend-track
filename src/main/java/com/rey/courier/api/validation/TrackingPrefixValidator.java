package com.rey.courier.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// 1. Implements ConstraintValidator<TheAnnotation, TheDataType>
public class TrackingPrefixValidator implements ConstraintValidator<ValidTrackingPrefix, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        
        // 2. Best Practice: Let @NotNull handle nulls. 
        // If it's null, we return true here so we don't accidentally trigger a NullPointerException.
        if (value == null) {
            return true; 
        }

        // 3. The actual business rule!
        return value.startsWith("TRK-");
    }
}