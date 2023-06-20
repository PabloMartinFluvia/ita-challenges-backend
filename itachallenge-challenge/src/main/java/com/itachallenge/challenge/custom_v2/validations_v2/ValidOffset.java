package com.itachallenge.challenge.custom_v2.validations_v2;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Min;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Min(value = 0) //doesn't have sense exclude a negative value
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@ReportAsSingleViolation
//https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#section-constraint-composition
public @interface ValidOffset {

    String message() default "{offset.error}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
